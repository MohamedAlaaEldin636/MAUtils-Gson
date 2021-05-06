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

package com.maproductions.mohamedalaa.sample.core.del_later;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class J1 {

    public void m1() {
        List<? super String> l1 = new ArrayList<>();
        l1.add("");
        Object oo = "";
        //l1.add(oo);
        CharSequence a = ((String) "2");

        String[] a1 = new String[]{"", ""};
        System.out.println(a1[0]);
        Object[] a2 = a1;
        System.out.println(a2[0]);

        /*List<String> strs = new ArrayList<String>();
        List<Object> objs = strs; // !!! A compile-time error here saves us from a runtime exception later.
        objs.add(1); // Put an Integer into a list of Strings
        String s = strs.get(0); // */
        List<Object> objs = new ArrayList<>();
        objs.add(1);
        //objs.addAll(1);
        for (Object o : objs) {

        }
    }

}
