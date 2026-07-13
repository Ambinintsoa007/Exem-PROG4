package hei.school.arith.endpoint.event.consumer.model;

import hei.school.arith.PojaGenerated;
import hei.school.arith.endpoint.event.model.PojaEvent;

@PojaGenerated
public record TypedEvent(String typeName, PojaEvent payload) {}
