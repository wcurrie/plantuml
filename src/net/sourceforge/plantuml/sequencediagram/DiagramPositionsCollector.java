package net.sourceforge.plantuml.sequencediagram;

import java.util.*;

public class DiagramPositionsCollector {
    public enum Direction {
        Send, Receive
    }

    private static DiagramPositionsCollector instance = new DiagramPositionsCollector();

    private Map<String, Set<MessageEvent>> positionsPerParticipant = new HashMap<String, Set<MessageEvent>>();

    public static DiagramPositionsCollector getInstance() {
        return instance;
    }

    public void recordMessage(String leftParticipantCode, String rightParticipantCode, double yPosition, Iterable<CharSequence> label) {
        positionFor(leftParticipantCode).add(new MessageEvent(Direction.Send, yPosition, label));
        positionFor(rightParticipantCode).add(new MessageEvent(Direction.Receive, yPosition, label));
    }

    public Map<String, Set<MessageEvent>> getPositionsPerParticipant() {
        return positionsPerParticipant;
    }

    private Set<MessageEvent> positionFor(String participantCode) {
        Set<MessageEvent> messageEvents = positionsPerParticipant.get(participantCode);
        if (messageEvents == null) {
            messageEvents = new LinkedHashSet<MessageEvent>();
            positionsPerParticipant.put(participantCode, messageEvents);
        }
        return messageEvents;
    }

    public static class MessageEvent {
        private final Direction direction;
        private final double position;
        private final Iterable<CharSequence> label;

        private MessageEvent(Direction direction, double position, Iterable<CharSequence> label) {
            this.direction = direction;
            this.position = position;
            this.label = label;
        }

        public Direction getDirection() {
            return direction;
        }

        public double getPosition() {
            return position;
        }

        public Iterable<CharSequence> getLabel() {
            return label;
        }

        public String joinedLabel() {
            StringBuilder b = new StringBuilder();
            if (label != null) {
                for (CharSequence s : label) {
                    if (b.length() > 0) {
                        b.append('\n');
                    }
                    b.append(s);
                }
            }
            return b.toString();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MessageEvent that = (MessageEvent) o;

            if (Double.compare(that.position, position) != 0) return false;
            if (direction != that.direction) return false;
            if (!joinedLabel().equals(that.joinedLabel())) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result;
            long temp;
            result = direction != null ? direction.hashCode() : 0;
            temp = position != +0.0d ? Double.doubleToLongBits(position) : 0L;
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            result = 31 * result + joinedLabel().hashCode();
            return result;
        }
    }
}
