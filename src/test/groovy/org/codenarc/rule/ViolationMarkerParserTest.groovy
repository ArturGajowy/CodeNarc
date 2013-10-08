package org.codenarc.rule

import org.junit.Test

class ViolationMarkerParserTest {
    
    ViolationMarkerParser parser = new ViolationMarkerParser()
    Closure violationMarker = { String message -> new ViolationMarker(message) }
    def violatingLine = /println 'Hello, World!'/
    def violationMessage = /Sir, that's a violation!/

    //irrelevant
    @Test
    void testMarkersPlayNicelyWithMultiLineComments() { assert false }
    
    //allow escaping, escape by default
    /*
    * 
    * ${v('1#!')}${v('2\\')}
    * #!1\#\!#!2\
    * 
    * 
    * 
    * */
    @Test 
    void testMarkersDoNotInterfereWithPossibleViolationMessageText() {
        """
        println 'penguin'       #'penguin' is a swearword. Ask on \\#kernelnewbies why.  
        """
        assert false 
    }
    
    //unfeasible, irrelevant
    //that could be an argument to make markers long (to prevent verbatim usage).
    //Nevertheless, mixing verbatim and interpolated markers is a bad style and asking for trouble
    @Test
    void testMixingVerbatimAndInterpolatedMarkersCantGoWrong() {assert false}
                                                                  
    @Test 
    void testSheBangsNotInterpretedAsMarkers() {assert false}
    
    //irrelevant
    @Test 
    void testStringContentsNotInterpretedAsMarkers() {
        "#ooops"
        
        "#TODO comment in generated code"
        
        """multi-line
        strings
        will            
        be
        #painfull...
        """
        assert false
    }
    
    //irrelevant
    @Test 
    void testCommentContentsNotInterpretedAsMarkers() {
        /* #a violation? not really! a color! #ffffff */
        assert false
    }

    @Test
    void testFindsNoViolations() {
        String source = '''
            class NoViolations { /* but still, there are comments */
                
            }
        '''
        def (violations, sourceWithoutMarkers) = parser.parse(source)
        assert violations == []
        assert sourceWithoutMarkers == source
    }
    
    @Test
    void testFindsNoViolationsInEmptySource() {
        String source = ''
        def (violations, sourceWithoutMarkers) = parser.parse(source)
        assert violations == []
        assert sourceWithoutMarkers == source
    }
    
    @Test
    void testFindsNoViolationsInAllWhitespaceSource() {
        String source = '''

        '''
        def (violations, sourceWithoutMarkers) = parser.parse(source)
        assert violations == []
        assert sourceWithoutMarkers == source
    }

    @Test
    void testFindsSingleViolation() {
        def (violations, sourceWithoutMarkers) = parser.parse("$violatingLine ${violationMarker(violationMessage)}")
        assert violations == [createViolation(1, violatingLine, violationMessage)]
        assert sourceWithoutMarkers == violatingLine + ' '
    }

    @Test
    void testFindsViolationsInMultipleLines() {
        def (violations, sourceWithoutMarkers) = parser.parse("""
            class TwoViolations {   ${violationMarker('violation 1')}
                String foo          #this #is #dumb
            }                       ${violationMarker('violation 2')}
        """)
        assert violations == [
            createViolation(2, 'class TwoViolations {', 'violation 1'),
            createViolation(4, '}',                     'violation 2'),
        ]
        
        assert sourceWithoutMarkers == """
            class TwoViolations {   
                String foo    
            }                       
        """
    }

    @Test
    void testFindsMultipleViolationsPerLine() {
        def (violations, sourceWithoutMarkers) = parser.parse("""
            class TwoViolations {   ${violationMarker('violation 1')}${violationMarker('violation 2')}
                String foo          ${violationMarker('violation 3')}       ${violationMarker('violation 4')}    
            }                       
        """)
        assert violations == [
            createViolation(2, 'class TwoViolations {', 'violation 1'),
            createViolation(2, 'class TwoViolations {', 'violation 2'),
            createViolation(4, 'String foo',            'violation 3'),
            createViolation(4, 'String foo',            'violation 4'),
        ]
        
        assert sourceWithoutMarkers == """
            class TwoViolations {   
                String foo          
            }                       
        """
    }

    Map createViolation(int lineNumber, String sourceLineText, String messageText) {
        return [lineNumber: lineNumber, sourceLineText: sourceLineText, messageText: messageText]
    }
}
