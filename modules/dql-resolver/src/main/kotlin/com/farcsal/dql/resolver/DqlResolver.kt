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
package com.farcsal.dql.resolver

import com.farcsal.dql.model.DqlExpression
import com.farcsal.dql.resolver.generated.DqlLexer
import com.farcsal.dql.resolver.generated.DqlParser
import com.farcsal.dql.resolver.variable.DefaultDqlVariableResolver
import com.farcsal.dql.resolver.variable.DqlVariableResolver
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.ConsoleErrorListener
import org.antlr.v4.runtime.tree.ParseTreeWalker

class DqlResolver(private val variableResolver: DqlVariableResolver = DefaultDqlVariableResolver) {

    fun resolve(query: String?): DqlExpression {
        val input = CharStreams.fromString(query)
        val lexer = DqlLexer(input)
        lexer.removeErrorListener(ConsoleErrorListener.INSTANCE)
        lexer.addErrorListener(DqlResolverErrorListener())
        val tokens = CommonTokenStream(lexer)
        val parser = DqlParser(tokens)
        parser.removeErrorListener(ConsoleErrorListener.INSTANCE)
        parser.addErrorListener(DqlResolverErrorListener())
        val sentence: DqlParser.SentenceContext = parser.sentence()
        val listener = DqlResolverListener(variableResolver = variableResolver)
        ParseTreeWalker.DEFAULT.walk(listener, sentence)
        return listener.getResult()
    }

}
