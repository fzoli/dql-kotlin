/*
 * Copyright 2022 Zoltan Farkas
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.farcsal.dql.query.parser

import com.farcsal.dql.model.DqlCriteriaValue
import com.farcsal.dql.model.DqlNumber
import com.farcsal.dql.query.parser.filter.DqlFilterFactory
import com.farcsal.dql.query.parser.sample.filter.PersonDqlFilterFieldExpressionResolver
import com.farcsal.dql.query.parser.sample.filter.PersonFilterFieldFactory
import com.farcsal.dql.resolver.variable.DqlVariableResolver
import com.querydsl.core.types.dsl.Expressions
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class DqlFilterFactoryTest {

    @Test
    fun groupsWithNot() {
        val factory = DqlFilterFactory()
        val field = PersonFilterFieldFactory.createPersonField()
        val resolver = PersonDqlFilterFieldExpressionResolver(field)
        val filter = "!(name.first_name:eq:\"a\"&name.last_name:memberOf:[\"b\"])|!(age:gt:20)"
        val dsl = "!(first_name = a && last_name = b) || !(age > 20)"
        val result = factory.create(filter, resolver)
        Assertions.assertEquals(dsl, result.toString())
    }

    @Test
    fun rightGroupWithoutNot() {
        val factory = DqlFilterFactory()
        val field = PersonFilterFieldFactory.createPersonField()
        val resolver = PersonDqlFilterFieldExpressionResolver(field)
        val filter = "age:gt:1&(age:gt:2|age:gt:3)"
        val dsl = "age > 1 && (age > 2 || age > 3)"
        val result = factory.create(filter, resolver)
        Assertions.assertEquals(dsl, result.toString())
    }

    @Test
    fun leftGroupWithoutNot() {
        val factory = DqlFilterFactory()
        val field = PersonFilterFieldFactory.createPersonField()
        val resolver = PersonDqlFilterFieldExpressionResolver(field)
        val filter = "(age:gt:2|age:gt:3)&age:gt:1"
        val dsl = "(age > 2 || age > 3) && age > 1"
        val result = factory.create(filter, resolver)
        Assertions.assertEquals(dsl, result.toString())
    }

    @Test
    fun resolveNumberVariable() {
        val myNumber = if (Math.random() > 0.5) 5 else 10
        val myNumberVariableResolver = object : DqlVariableResolver {
            override fun resolveVariable(name: String): DqlCriteriaValue {
                if (name == "myNumber") {
                    return DqlCriteriaValue.ofNumber(DqlNumber(myNumber))
                }
                throw unknownVariable(name)
            }
        }
        val factory = DqlFilterFactory(variableResolver = myNumberVariableResolver)
        val field = PersonFilterFieldFactory.createPersonField()
        val resolver = PersonDqlFilterFieldExpressionResolver(field)
        val filter = "age:gt:{myNumber}"
        val dsl = "age > $myNumber"
        val result = factory.create(filter, resolver)
        Assertions.assertEquals(dsl, result.toString())
    }

    @Test
    fun keepsPrecedence1() {
        val factory = DqlFilterFactory()
        val field = PersonFilterFieldFactory.createPersonField()
        val resolver = PersonDqlFilterFieldExpressionResolver(field)
        val filter = "age:gt:1|age:gt:2&age:gt:3"
        val dsl = "age > 1 || age > 2 && age > 3"
        val result = factory.create(filter, resolver)
        Assertions.assertEquals(dsl, result.toString())
    }

    @Test
    fun keepsPrecedence2() {
        val factory = DqlFilterFactory()
        val field = PersonFilterFieldFactory.createPersonField()
        val resolver = PersonDqlFilterFieldExpressionResolver(field)
        val filter = "age:gt:1&age:gt:2|age:gt:3&age:gt:4"
        val dsl = "age > 1 && age > 2 || age > 3 && age > 4"
        val result = factory.create(filter, resolver)
        Assertions.assertEquals(dsl, result.toString())
    }

    @Test
    fun keepsPrecedence3() {
        val factory = DqlFilterFactory()
        val field = PersonFilterFieldFactory.createPersonField()
        val resolver = PersonDqlFilterFieldExpressionResolver(field)
        val filter = "age:gt:1|(age:gt:2|age:gt:3&age:gt:4)&age:gt:5"
        val dsl = "age > 1 || (age > 2 || age > 3 && age > 4) && age > 5"
        val result = factory.create(filter, resolver)
        Assertions.assertEquals(dsl, result.toString())
    }

    @Test
    fun keepsPrecedence4() {
        val factory = DqlFilterFactory()
        val field = PersonFilterFieldFactory.createPersonField()
        val resolver = PersonDqlFilterFieldExpressionResolver(field)
        val filter = "age:gt:1|(age:gt:2|!(age:gt:3|age:gt:4)&age:gt:5)&age:gt:6"
        val dsl = "age > 1 || (age > 2 || !(age > 3 || age > 4) && age > 5) && age > 6"
        val result = factory.create(filter, resolver)
        Assertions.assertEquals(dsl, result.toString())
        val age = field.age
        val dslExpr = age.gt(1).or(
            age.gt(2).or(
                age.gt(3).or(age.gt(4)).not().and(age.gt(5))
            ).and(age.gt(6))
        )
        Assertions.assertEquals(dsl, dslExpr.toString())
    }

    @Test
    fun keepsPrecedence5() {
        val factory = DqlFilterFactory()
        val field = PersonFilterFieldFactory.createPersonField()
        val resolver = PersonDqlFilterFieldExpressionResolver(field)
        val filter = "age:gt:1|(age:gt:2|(age:gt:3|age:gt:4)&age:gt:5)&age:gt:6"
        val dsl = "age > 1 || (age > 2 || (age > 3 || age > 4) && age > 5) && age > 6"
        val result = factory.create(filter, resolver)
        Assertions.assertEquals(dsl, result.toString())
        val age = field.age
        val dslExpr = age.gt(1).or(
            age.gt(2).or(
                age.gt(3).or(age.gt(4)).and(age.gt(5))
            ).and(age.gt(6))
        )
        Assertions.assertEquals(dsl, dslExpr.toString())
    }

    @Test
    fun keepsPrecedence6() {
        val factory = DqlFilterFactory()
        val field = PersonFilterFieldFactory.createPersonField()
        val resolver = PersonDqlFilterFieldExpressionResolver(field)
        val filter = "age:gt:1|((age:gt:2|age:gt:3)&age:gt:4)|age:gt:6"
        val dsl = "age > 1 || (age > 2 || age > 3) && age > 4 || age > 6"
        val result = factory.create(filter, resolver)
        Assertions.assertEquals(dsl, result.toString())
    }

    @Test
    fun keepsPrecedence7() {
        val factory = DqlFilterFactory()
        val field = PersonFilterFieldFactory.createPersonField()
        val resolver = PersonDqlFilterFieldExpressionResolver(field)
        val filter = "age:gt:1|(age:gt:2|(age:gt:3&age:gt:4))&age:gt:6"
        val dsl = "age > 1 || (age > 2 || age > 3 && age > 4) && age > 6"
        val result = factory.create(filter, resolver)
        Assertions.assertEquals(dsl, result.toString())
    }

    @Test
    fun keepsPrecedence8() {
        val factory = DqlFilterFactory()
        val field = PersonFilterFieldFactory.createPersonField()
        val resolver = PersonDqlFilterFieldExpressionResolver(field)
        val age = field.age
        val criteria = age.gt(1) or !(age.gt(2) or age.gt(3) and age.gt(4)) or age.gt(6)
        val filter = "age:gt:1|!((age:gt:2|age:gt:3)&age:gt:4)|age:gt:6"
        val dsl = "age > 1 || !((age > 2 || age > 3) && age > 4) || age > 6"
        val result = factory.create(filter, resolver)
        Assertions.assertEquals(dsl, result.toString())
        Assertions.assertEquals(dsl, criteria.toString())
    }

    @Test
    fun keepsPrecedence9() {
        val factory = DqlFilterFactory()
        val field = PersonFilterFieldFactory.createPersonField()
        val resolver = PersonDqlFilterFieldExpressionResolver(field)
        val age = field.age
        val criteria = age.gt(1) and ( age.gt(2) or age.gt(3) ) and age.gt(4)
        val filter = "age:gt:1&(age:gt:2|age:gt:3)&age:gt:4"
        val dsl = "age > 1 && (age > 2 || age > 3) && age > 4"
        val result = factory.create(filter, resolver)
        Assertions.assertEquals(dsl, result.toString())
        Assertions.assertEquals(dsl, criteria.toString())
    }

    @Test
    fun keepsPrecedence10() {
        val factory = DqlFilterFactory()
        val field = PersonFilterFieldFactory.createPersonField()
        val resolver = PersonDqlFilterFieldExpressionResolver(field)
        val filter = "age:gt:1|age:gt:2&age:gt:3|age:gt:4&age:gt:5"
        val dsl = "age > 1 || age > 2 && age > 3 || age > 4 && age > 5"
        val result = factory.create(filter, resolver)
        Assertions.assertEquals(dsl, result.toString())
    }

    @Test
    fun keepsPrecedence11() {
        val factory = DqlFilterFactory()
        val field = PersonFilterFieldFactory.createPersonField()
        val resolver = PersonDqlFilterFieldExpressionResolver(field)
        val filter = "age:gt:1|(age:gt:2&age:gt:3)|age:gt:4&age:gt:5"
        val dsl = "age > 1 || age > 2 && age > 3 || age > 4 && age > 5"
        val result = factory.create(filter, resolver)
        Assertions.assertEquals(dsl, result.toString())
    }

    @Test
    fun keepsPrecedence12() {
        val factory = DqlFilterFactory()
        val field = PersonFilterFieldFactory.createPersonField()
        val resolver = PersonDqlFilterFieldExpressionResolver(field)
        val filter = "age:gt:1|(age:gt:2&age:gt:3|age:gt:4&age:gt:5)"
        val dsl = "age > 1 || age > 2 && age > 3 || age > 4 && age > 5"
        val result = factory.create(filter, resolver)
        Assertions.assertEquals(dsl, result.toString())
        val age = Expressions.numberPath(Long::class.javaObjectType, "age")
        val dslExpr = age.gt(1).or(
            age.gt(2).and(age.gt(3)).or(age.gt(4).and(age.gt(5)))
        )
        Assertions.assertEquals(dsl, dslExpr.toString())
    }

}
