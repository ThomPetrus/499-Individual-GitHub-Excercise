#!/bin/bash

show_help() {
  printf "\nUsage: $0 COMMAND [--OPTION]\n\n"
  printf "Commands:\n"
  printf "  package               package the application jar file\n"
  printf "  run                   run the application locally\n"
  printf "  test                  run the tests\n"
}

package() {
  mvn clean package
}

run() {
  mvn clean package
  docker-compose up --build
}

test() {
  mvn test
}

if [ "$#" -lt 1 ]; then
  show_help
  exit 1
fi

case "$1" in
  run)
    run
  ;;
  package)
    package
  ;;
  test)
    test
  ;;
  *)
    printf "\nUnknown option for the command: $1\n"
    show_help
    exit 2
  ;;
esac

exit 0