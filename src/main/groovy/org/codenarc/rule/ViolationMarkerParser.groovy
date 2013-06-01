package org.codenarc.rule

import static java.lang.Math.max

class ViolationMarkerParser {

    
    ParseResult parse(String source) {
        String[] lines = source.split('\n')
        int lineNumber = 1
        List<ParseResult> lineResults = lines.collect { String line ->
            parseLine(line, lineNumber++)
        }
        return new ParseResult(
            violations: lineResults*.violations.flatten(),
            sourceWithoutMarkers: lineResults*.sourceWithoutMarkers.join('\n')
        )
    }

    static class ParseResult {
        private List<Map> violations = []
        private String sourceWithoutMarkers = '' 

        def getAt(int index) {
            [violations, sourceWithoutMarkers][index]
        }

        void merge(ParseResult result) {
            violations.addAll(result.violations)
            sourceWithoutMarkers += '\n' + result.sourceWithoutMarkers
        }
    }

    private static ParseResult parseLine(String line, Integer lineNumber) {
        def markerStartIndex = line.indexOf(ViolationMarker.MARKER_START)
        def markerEndIndex = line.indexOf(ViolationMarker.MARKER_END)
        def sourceWithoutMarkersEnd = [markerStartIndex, line.length()].findAll { it >= 0 }.min()
        def (sourceWithoutMarkers, markers) = splitAt(sourceWithoutMarkersEnd, line)
        def markersDisabled = sourceWithoutMarkers.reverse().matches(~$/\s*///$)

        
        
        List<Map> violations = parseViolations(markers)
        
        return new ParseResult(violations: violations, sourceWithoutMarkers: sourceWithoutMarkers)
    }

    static List<Map> parseViolations(String markers) {
        return parseViolationStart(markers)
    }

    static List<Map> parseViolationStart(String markers) {
        assert markers.startsWith(ViolationMarker.MARKER_START)
        return null;  //To change body of created methods use File | Settings | File Templates.
    }

    private def splitAt(int index, String string) {
        [string[0 .. max(0, index - 1)], string.substring(index)]
    }
}
