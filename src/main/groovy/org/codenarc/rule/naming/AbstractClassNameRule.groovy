/*
 * Copyright 2009 the original author or authors.
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
package org.codenarc.rule.naming

import org.codehaus.groovy.ast.ClassNode
import org.codenarc.rule.AbstractAstVisitor
import org.codenarc.rule.AbstractAstVisitorRule
import org.codenarc.rule.Violation

/**
 * Rule that verifies that the name of an abstract class mathes a regular expression. By default it 
 * checks that the class name starts with the 'Abstract' prefix, followed by an uppercase letter and
 * then zero or more word characters (letters, numbers or underscores).
 * <p/>
 * The <code>regex</code> property specifies the regular expression to check the abstract class name against.
 * It is required and cannot be null or empty. It defaults to 'Abstract[A-Z]\w*'.
 *
 * @author Chris Mair
 * @version $Revision: 37 $ - $Date: 2009-02-06 21:31:05 -0500 (Fri, 06 Feb 2009) $
 */
class AbstractClassNameRule extends AbstractAstVisitorRule {
    String name = 'AbstractClassName'
    int priority = 2
    Class astVisitorClass = AbstractClassNameAstVisitor
    String regex = /Abstract[A-Z]\w*/
}

class AbstractClassNameAstVisitor extends AbstractAstVisitor  {
    void visitClass(ClassNode classNode) {
        assert rule.regex
        def isAbstract = classNode.modifiers & classNode.ACC_ABSTRACT
        if (isAbstract && !(classNode.getNameWithoutPackage() ==~ rule.regex)) {
            addViolation(classNode)
        }
        super.visitClass(classNode)
    }

}