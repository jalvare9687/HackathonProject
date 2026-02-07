import React from 'react';

export default function EventTag({ event }) {
  if (!event) {
    return <span className="event-tag none">None</span>;
  }
  return <span className="event-tag active">{event}</span>;
}
