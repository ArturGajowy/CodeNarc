/*
 * Copyright 2012 the original author or authors.
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
package org.codenarc.rule.size

import org.codenarc.rule.AbstractAstVisitorRule

import org.gmetrics.metric.crap.CrapMetric
import org.gmetrics.metric.coverage.CoberturaLineCoverageMetric

import java.util.concurrent.atomic.AtomicBoolean
import org.apache.log4j.Logger

/**
 * Rule that calculates the CRAP Metric for methods/classes and checks against
 * configured threshold values.
 * <p/>
 * Note that this rule requires the GMetrics 0.5 (or later) jar on the classpath, as well as
 * a Cobertura XML coverage file. If either of these prerequisites is not available, this rule
 * logs a warning messages and exits (i.e., does nothing).
 * <p/>
 * The <code>maxMethodCrapScore</code> property holds the threshold value for the CRAP crapMetric
 * value for each method. If this value is non-zero, a method with a CRAP score value greater than
 * this value is considered a violation. The <code>maxMethodCrapScore</code> property defaults to 30.
 * <p/>
 * The <code>maxClassAverageCrapScore</code> property holds the threshold value for the average CRAP
 * crapMetric value for each class. If this value is non-zero, a class with an average CRAP score
 * value greater than this value is considered a violation. The <code>maxMethodAverageCrapScore</code> property
 * defaults to 30.
 * <p/>
 * The <code>ignoreMethodNames</code> property optionally specifies one or more (comma-separated) method
 * names that should be ignored (i.e., that should not cause a rule violation). The name(s) may optionally
 * include wildcard characters ('*' or '?'). Note that the ignored methods still contribute to the class
 * complexity value.
 * <p/>
 * This rule does NOT treat "closure fields" as methods (unlike some of the other size/complexity rules).
 *
 * @see <a href="http://www.artima.com/weblogs/viewpost.jsp?thread=210575">The original 2007 blog post that defined the CRAP crapMetric</a>.
 * @see <a href="http://googletesting.blogspot.com/2011/02/this-code-is-crap.html">A 2011 blog post from Alberto Savoia, describing the formula, the motivation, and the CRAP4J tool</a>.
 * @see <a href="http://gmetrics.sourceforge.net/gmetrics-CrapMetric.html">GMetrics CRAP crapMetric</a>.
 *
 * @author Chris Mair
  */
class CrapMetricRule extends AbstractAstVisitorRule {

    private static final LOG = Logger.getLogger(CrapMetricRule)

    String name = 'CrapMetric'
    int priority = 2
    BigDecimal maxMethodCrapScore = 30
    BigDecimal maxClassAverageMethodCrapScore = 30
    String coberturaXmlFile
    String ignoreMethodNames
    Class astVisitorClass = CrapMetricAstVisitor

    protected String crapMetricClassName = 'org.gmetrics.metric.crap.CrapMetric'
    private final AtomicBoolean hasNotReadyWarningBeenLogged = new AtomicBoolean(false)

    @Override
    boolean isReady() {
        def ready = coberturaXmlFile && isCrapMetricClassOnClasspath()
        if (!ready && !hasNotReadyWarningBeenLogged.value) {
            LOG.warn("Cobertura XML file [$coberturaXmlFile] is not accessible and/or GMetrics CrapMetric class is not on the classpath; skipping this rule")
            hasNotReadyWarningBeenLogged.set(true)
        }
        return ready
    }

    private boolean isCrapMetricClassOnClasspath() {
        try {
            getClass().classLoader.loadClass(crapMetricClassName)
            return true
        }
        catch (ClassNotFoundException e) {
            return false
        }
    }
}

class CrapMetricAstVisitor extends AbstractMethodMetricAstVisitor  {

    final String metricShortDescription = 'CRAP score'

    protected Object createMetric() {
        def coverageMetric = new CoberturaLineCoverageMetric(coberturaFile:rule.coberturaXmlFile)
        return new CrapMetric(coverageMetric:coverageMetric)
    }

    protected Object getMaxMethodMetricValue() {
        rule.maxMethodCrapScore
    }

    protected Object getMaxClassMetricValue() {
        rule.maxClassAverageMethodCrapScore
    }
}