#!/bin/bash
while inotifywait -e close_write,moved_to,create .; do sleep 0.5 && make; done
