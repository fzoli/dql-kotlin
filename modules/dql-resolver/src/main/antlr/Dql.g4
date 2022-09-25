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

/*
 * Use the ANTLR4 idea plugin for easier visualization.
 */
grammar Dql;

/*
 * Parser Rules
 */

sentence: (expression | criteria) (logicalOperator (expression | criteria))*;
expression: NOT? '(' (expression | criteria) (logicalOperator (expression | criteria))* ')';
criteria: NOT? (binaryCriteria | unaryCriteria);
binaryCriteria: field BINARY_OPERATION value;
unaryCriteria: field UNARY_OPERATION;
field: WORD;
value: LITERAL | ARRAY;
logicalOperator: LOGICAL;

/*
 * Lexer Rules
 */

fragment LOWERCASE: [a-z];
fragment UPPERCASE: [A-Z];
fragment DIGIT: [0-9];
fragment VALUE_CHAR: '\\"' | ~('"');
fragment STRING_LITERAL: '"'VALUE_CHAR*'"';
fragment NUMBER_LITERAL: '0' | '-'?[1-9]DIGIT* | '-'?DIGIT'.'DIGIT*;
fragment VARIABLE_LITERAL: '{'~('}')*'}';

BINARY_OPERATION: ':'(LOWERCASE | UPPERCASE | DIGIT)+':';
UNARY_OPERATION: ':'(LOWERCASE | UPPERCASE | DIGIT)+;
LITERAL: STRING_LITERAL | NUMBER_LITERAL | VARIABLE_LITERAL;
ARRAY: '[]' | '['(LITERAL',')*LITERAL']';
WORD: (LOWERCASE | UPPERCASE | DIGIT | '_' | '.')+;
LOGICAL: ('&' | '|');
NOT: '!';
