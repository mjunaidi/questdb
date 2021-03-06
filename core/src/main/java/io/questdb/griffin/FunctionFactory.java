/*******************************************************************************
 *     ___                  _   ____  ____
 *    / _ \ _   _  ___  ___| |_|  _ \| __ )
 *   | | | | | | |/ _ \/ __| __| | | |  _ \
 *   | |_| | |_| |  __/\__ \ |_| |_| | |_) |
 *    \__\_\\__,_|\___||___/\__|____/|____/
 *
 *  Copyright (c) 2014-2019 Appsicle
 *  Copyright (c) 2019-2020 QuestDB
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 ******************************************************************************/

package io.questdb.griffin;

import io.questdb.cairo.CairoConfiguration;
import io.questdb.cairo.sql.Function;
import io.questdb.std.ObjList;
import io.questdb.std.Transient;

public interface FunctionFactory {
    /**
     * Function signature in a form of "name(type...)". Name is a literal that does not
     * start with number and contains no control characters, which can be confused with
     * SQL language punctuation. Control characters include but not limited to:
     * ',', '(', ')', '*', '/', '%', '+', '-', '='.
     * <p>
     * Argument types are represented by single character from this table:
     * <ul>
     * <li>B = byte</li>
     * <li>E = short</li>
     * <li>I = int</li>
     * <li>L = long</li>
     * <li>F = float</li>
     * <li>D = double</li>
     * <li>S = string</li>
     * <li>A = char</li>
     * <li>K = symbol</li>
     * <li>T = boolean</li>
     * <li>M = date</li>
     * <li>N = timestamp</li>
     * <li>U = binary</li>
     * <li>V = variable argument list</li>
     * <li>C = cursor</li>
     * <li>H = long256</li>
     * </ul>
     *
     * @return signature, for example "substr(SII)"
     */
     String getSignature();

    default boolean isGroupBy() { return false; }

    Function newInstance(
            @Transient ObjList<Function> args,
            int position,
            CairoConfiguration configuration
    ) throws SqlException;
}
