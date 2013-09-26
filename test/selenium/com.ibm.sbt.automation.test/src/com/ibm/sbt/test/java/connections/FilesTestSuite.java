/*
 * � Copyright IBM Corp. 2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
package com.ibm.sbt.test.java.connections;

import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.ibm.sbt.automation.core.environment.TestEnvironment;
import com.ibm.sbt.test.java.connections.files.AddCommentToFile;
import com.ibm.sbt.test.java.connections.files.AddRemoveTag;
import com.ibm.sbt.test.java.connections.files.CreateDeleteFile;
import com.ibm.sbt.test.java.connections.files.GetFileComments;
import com.ibm.sbt.test.java.connections.files.GetFilesSharedByMe;
import com.ibm.sbt.test.java.connections.files.GetFilesSharedWithMe;
import com.ibm.sbt.test.java.connections.files.GetMyFiles;
import com.ibm.sbt.test.java.connections.files.LockUnlockFile;

/**
 * @author mwallace
 * 
 * @date 6 Mar 2013
 */
@RunWith(Suite.class)
@SuiteClasses({ AddCommentToFile.class, AddRemoveTag.class, CreateDeleteFile.class, GetFileComments.class, GetFilesSharedByMe.class, GetFilesSharedWithMe.class,
        GetMyFiles.class, LockUnlockFile.class })
public class FilesTestSuite {
    @AfterClass
    public static void cleanup() {
        TestEnvironment.cleanup();
    }
}
