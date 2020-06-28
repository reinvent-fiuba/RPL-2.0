#include <criterion/criterion.h>
#include "main.c"

Test(misc, test_name_1) {
    cr_assert(f1() == 1);
}

Test(misc, test_name_2) {
    cr_assert(f2() == 2);
}