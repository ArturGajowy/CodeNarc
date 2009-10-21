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
package org.gmetrics.metric.loc

import org.codehaus.groovy.ast.MethodNode
import org.codehaus.groovy.ast.expr.ClosureExpression
import org.gmetrics.metric.NumberMetricResult
import org.gmetrics.metric.AbstractMethodMetric
import org.gmetrics.results.ClassMetricResult
import org.codehaus.groovy.ast.ClassNode
import org.gmetrics.metric.AggregateNumberMetricResult
import org.gmetrics.util.AstUtil

/**
 * Metric for counting the lines of code for methods and closure fields.
 *
 * @author Chris Mair
 * @version $Revision$ - $Date$
 */
class MethodLinesOfCodeMetric extends AbstractMethodMetric {

    def calculate(MethodNode methodNode) {
        def visitor = new MethodLinesOfCodeAstVisitor(sourceCode:sourceCode)
        visitor.visitMethod(methodNode)
        return new NumberMetricResult(this, visitor.numberOfLinesInMethod)
    }

    def calculate(ClosureExpression closureExpression) {
        def visitor = new MethodLinesOfCodeAstVisitor(sourceCode:sourceCode)
        visitor.visitClosureExpression(closureExpression)
        return new NumberMetricResult(this, visitor.numberOfLinesInClosure)
    }

}
