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
package com.farcsal.query.dql

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class DqlGroupingTest {

    private val fs = DqlStringField("s")
    private val a = fs.eq("a")
    private val b = fs.eq("b")
    private val c = fs.eq("c")

    @Test
    fun `(a+b)&c grouping`() {
        Assertions.assertEquals("""(s:eq:"a"|s:eq:"b")&s:eq:"c"""", a.or(b).and(c).toDql())
    }

    @Test
    fun `a&b+c grouping`() {
        Assertions.assertEquals("""s:eq:"a"&s:eq:"b"|s:eq:"c"""", a.and(b).or(c).toDql())
    }

    @Test
    fun `!(a&b)+c grouping`() {
        Assertions.assertEquals("""!(s:eq:"a"&s:eq:"b")|s:eq:"c"""", a.and(b).not().or(c).toDql())
    }

    @Test
    fun `a+b&c grouping`() {
        Assertions.assertEquals("""s:eq:"a"|s:eq:"b"&s:eq:"c"""", a.or(b.and(c)).toDql())
    }

    @Test
    fun `a+!(b&c) grouping`() {
        Assertions.assertEquals("""s:eq:"a"|!(s:eq:"b"&s:eq:"c")""", a.or(b.and(c).not()).toDql())
    }

    private fun DqlCriteria.toDql(): String {
        return this.toString()
    }

}
