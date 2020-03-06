#include <criterion/criterion.h>
#include "main.c"

Test(misc, failing) {
    cr_assert(fooNoRepetido() == 1);
}

Test(misc, passing) {
    cr_assert(barNoRepetido() == 2);
}