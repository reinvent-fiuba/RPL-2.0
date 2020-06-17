#include <criterion/criterion.h>
#include "main.c"

Test(misc, testName1) {
    cr_assert(f1() == 1);
}

Test(misc, testName2) {
    cr_assert(f2() == 2);
}