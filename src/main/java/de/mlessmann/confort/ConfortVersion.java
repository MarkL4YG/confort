package de.mlessmann.confort;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ConfortVersion implements Comparable<ConfortVersion> {

    private static final Pattern EXTRACT_VERSION_PATTERN = Pattern.compile("((?:\\d+\\.?)+)");

    public static ConfortVersion parseString(String representation) {
        Matcher matcher = EXTRACT_VERSION_PATTERN.matcher(representation);
        if (!matcher.find()) {
            String group = matcher.group(1);
            String[] versionParts = group.split("\\.");
            List<Integer> parts = new LinkedList<>();

            for (String part : versionParts) {
                parts.add(Integer.valueOf(part));
            }

            return new ConfortVersion(parts);

        } else {
            String message = String.format("No version-like string has been found in %s with %s", representation, EXTRACT_VERSION_PATTERN.toString());
            throw new IllegalArgumentException(message);
        }
    }

    private List<Integer> parts = new LinkedList<>();

    protected ConfortVersion(List<Integer> parts) {
        this.parts = parts;
    }

    public ConfortVersion(Integer... parts) {
        this.parts.addAll(Arrays.asList(parts));
    }

    @Override
    @SuppressWarnings("squid:S1210")
    public int compareTo(ConfortVersion o) {
        if (o == null) return -1;

        int maxIter = Math.min(parts.size(), o.parts.size());

        Comparator<Object> intComparator = Comparator.comparingInt(Integer.class::cast);
        Iterator<Integer> myIterator = parts.iterator();
        Iterator<Integer> otherIterator = parts.iterator();
        for (int i = 0; i < maxIter; i++) {
            int local = myIterator.next();
            int other = otherIterator.next();
            int res = intComparator.compare(local, other);

            if (res != 0) {
                return res;
            }
        }

        if (myIterator.hasNext()) {
            return -1;
        } else if (otherIterator.hasNext()) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return parts.stream()
                .map(Object::toString)
                .collect(Collectors.joining("."));
    }
}
