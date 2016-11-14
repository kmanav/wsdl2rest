package org.slosc.wsdl2rest.test.rpclit;
/*
 * Copyright (c) 2008 SL_OpenSource Consortium
 * All Rights Reserved.
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
 *
 */

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.slosc.wsdl2rest.EndpointInfo;
import org.slosc.wsdl2rest.ClassGenerator;
import org.slosc.wsdl2rest.MethodInfo;
import org.slosc.wsdl2rest.ResourceMapper;
import org.slosc.wsdl2rest.WSDLProcessor;
import org.slosc.wsdl2rest.impl.ResourceMapperImpl;
import org.slosc.wsdl2rest.impl.WSDLProcessorImpl;
import org.slosc.wsdl2rest.impl.codegen.ClassGeneratorFactory;
import org.slosc.wsdl2rest.impl.codegen.JavaTypeGenerator;


public class GenerateRpcLitTest {

    @Test
    public void testGenerate() throws Exception {

        File wsdlFile = new File("src/test/resources/rpclit/Address.wsdl");
        Path outpath = new File("../tests/src/test/java").toPath();
        
        WSDLProcessor wsdlProcessor = new WSDLProcessorImpl();
        wsdlProcessor.process(wsdlFile.toURI());

        List<EndpointInfo> clazzDefs = wsdlProcessor.getClassDefinitions();
        Assert.assertEquals(1, clazzDefs.size());
        EndpointInfo clazzDef = clazzDefs.get(0);
        Assert.assertEquals("org.slosc.wsdl2rest.test.rpclit", clazzDef.getPackageName());
        Assert.assertEquals("Address", clazzDef.getClassName());

        List<MethodInfo> methods = clazzDef.getMethods();
        Assert.assertEquals(5, methods.size());
        
        ResourceMapper resMapper = new ResourceMapperImpl();
        resMapper.assignResources(clazzDefs);

        JavaTypeGenerator typeGen = new JavaTypeGenerator(outpath, wsdlFile.toURI().toURL());
        typeGen.execute();
        
        ClassGenerator gen = ClassGeneratorFactory.getClassGenerator(outpath);
        gen.generateClasses(clazzDefs);
    }
}