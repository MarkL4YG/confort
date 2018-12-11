package de.mlessmann.confort.migration.nodes;

import de.mlessmann.confort.api.IConfigNode;
import de.mlessmann.confort.migration.DescriptorScope;

public class AssignmentNode implements DeltaDescriptorNode {

    private boolean isWeak;
    private String key;
    private String value;

    public AssignmentNode(boolean isWeak, String key, String value) {
        this.isWeak = isWeak;
        this.key = key;
        this.value = value;
    }

    @Override
    public void readAhead(DescriptorScope scope) {
        set(scope);
    }

    @Override
    public void executeOn(IConfigNode root, IConfigNode relativeRoot, DescriptorScope scope) {
        set(scope);
    }

    private void set(DescriptorScope scope) {
        if (!scope.has(key) || !isWeak) {
            scope.set(key, value);
        }
    }
}
