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
package org.gmetrics.metric.abc

import org.gmetrics.source.SourceString
import org.gmetrics.test.AbstractTest

/**
 * Tests for AbcComplexityCalculator
 *
 * @author Chris Mair
 * @version $Revision$ - $Date$
 */
abstract class AbstractAbcTest extends AbstractTest {
    protected static final ZERO_VECTOR = [0, 0, 0]

    protected calculator

    void setUp() {
        super.setUp()
        calculator = new AbcComplexityCalculator()
    }

    protected void assertEquals(AbcVector abcVector, List expectedValues) {
        def actualValues = [abcVector.assignments, abcVector.branches, abcVector.conditions]
        assert actualValues == expectedValues
    }

    protected calculate(node) {
        def metricResult = calculator.calculate(node)
        log("metricResult=$metricResult")
        def abcVector = metricResult.abcVector
        return [abcVector.assignments, abcVector.branches, abcVector.conditions]
    }

    protected parseClass(String source) {
        def sourceCode = new SourceString(source)
        calculator.sourceCode = sourceCode
        def ast = sourceCode.ast
        return ast.classes[0]
    }

    protected AbcMetricResult abcMetricResult(int a, int b, int c) {
        def abcVector = new AbcVector(a, b, c)
        return new AbcMetricResult(abcVector)
    }

}