/*
 * Copyright 2010 the original author or authors.
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
package org.codenarc.results

import org.codenarc.rule.Violation

/**
 * This is a Results object for something that has no real file system, such as a string.
 *  
 * @author Hamlet D'Arcy
 */
class VirtualResults implements Results {

    List violations

    def VirtualResults(List violations) {
        this.violations = violations;
    }

    def String getPath() {
        throw new UnsupportedOperationException("Not supported on virtual results");
    }

    def List getChildren() {
        throw new UnsupportedOperationException("Not supported on virtual results");
    }

    def List getViolationsWithPriority(int priority) {
        violations.findAll { Violation violation ->
            violation.rule.priority == priority
        }
    }

    def int getNumberOfViolationsWithPriority(int priority, boolean recursive) {
        throw new UnsupportedOperationException("Not supported on virtual results");
    }

    def int getTotalNumberOfFiles(boolean recursive) {
        throw new UnsupportedOperationException("Not supported on virtual results");
    }

    def int getNumberOfFilesWithViolations(boolean recursive) {
        throw new UnsupportedOperationException("Not supported on virtual results");
    }

    def boolean isFile() {
        false
    }

    def Results findResultsForPath(String path) {
        throw new UnsupportedOperationException("Not supported on virtual results")
    }
}