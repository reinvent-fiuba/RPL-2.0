#include <criterion/criterion.h>
#include "main.c"

Test(misc, testName1) {
    cr_assert(fooNoRepetido() == 1);
}

Test(misc, testName2) {
    cr_assert(barNoRepetido() == 2);
}