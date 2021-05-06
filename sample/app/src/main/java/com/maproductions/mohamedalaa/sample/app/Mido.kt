/*
 * Copyright © 2020 Mohamed Alaa
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.maproductions.mohamedalaa.sample.app

import com.maproductions.mohamedalaa.annotation.MAProviderOfAbstracts
import com.maproductions.mohamedalaa.sample.core.normal_gson_same_field_name.AnnDC2

//@MAProviderOfAbstracts
class Mido {

    lateinit var string: String

    var int: Int = 0

    lateinit var annDC2: AnnDC2

    //lateinit var list: List<*> todo what if List<Int> will it only enters my serilizer if is List<Int> isa ?!
    // also before that check what the processor translates it to ?!?!

}