package org.codenarc.rule

@Immutable
class ViolationMarker {

    private static final String MARKER_START = '/* VIOLATION: '
    private static final String MARKER_END = ' END OF VIOLATION */'
    
    String violationMessage

    @Override
    String toString() {
        return MARKER_START + violationMessage + MARKER_END
    }
}
    
@Immutable
class MultipleViolationsMarker {
    List<ViolationMarker> violations

    @Override
    String toString() {
        return violations*.toString().join('')
    }
}
