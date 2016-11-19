#!/bin/bash

genpasswd() {
        local l=$1
        [ "$l" == "" ] && l=8
        tr -dc A-Za-z0-9_ < /dev/urandom | head -c ${l} | xargs
}

genpasswd