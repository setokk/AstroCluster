package edu.setokk.astrocluster.error;

import jakarta.validation.ElementKind;
import jakarta.validation.Path;

import java.util.Iterator;
import java.util.List;

public class AstroConstraintPath implements Path {
    private final List<Node> nodes;

    public AstroConstraintPath(String property) {
        this.nodes = List.of(new AstroNode(property));
    }

    @Override
    public Iterator<Node> iterator() {
        return nodes.iterator();
    }

    public static class AstroNode implements Node {
        private final String name;

        public AstroNode(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public boolean isInIterable() {
            return false;
        }

        @Override
        public Integer getIndex() {
            return 0;
        }

        @Override
        public Object getKey() {
            return null;
        }

        @Override
        public ElementKind getKind() {
            return null;
        }

        @Override
        public <T extends Node> T as(Class<T> aClass) {
            return null;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}

