/*
 * Copyright (c) 2009-2011 Panxiaobo
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.dex2jar.test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.dex2jar.v3.Main;

/**
 * @author Panxiaobo [pxb1988@gmail.com]
 * 
 */
public class ResTest {
    static final Logger log = LoggerFactory.getLogger(ResTest.class);

    @Test
    public void test() throws Exception {
        File dir = new File("target/test-classes/res");
        Map<String, List<File>> m = new HashMap();
        for (File f : FileUtils.listFiles(dir, new String[] { "class" }, false)) {
            String name = FilenameUtils.getBaseName(f.getName());

            int i = name.indexOf('$');
            String z = i > 0 ? name.substring(0, i) : name;
            List<File> files = m.get(z);
            if (files == null) {
                files = new ArrayList<File>();
                m.put(z, files);
            }
            files.add(f);
        }

        for (Entry<String, List<File>> e : m.entrySet()) {
            String name = e.getKey();
            log.info("Testing res file {}", name);
            File dex = TestUtils.dex(e.getValue(), new File(dir, name + ".dex"));
            File distFile = new File(dex.getParentFile(), FilenameUtils.getBaseName(dex.getName()) + "_dex2jar.jar");
            Main.doFile(dex, distFile);
            TestUtils.checkZipFile(distFile);
        }
        log.info("Done.");
    }
}