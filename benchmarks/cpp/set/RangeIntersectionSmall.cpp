#include <iostream>
#include <string>
#include "BUtils.cpp"
#include "BSet.cpp"
#include "BInteger.cpp"
#include "BBoolean.cpp"

#ifndef RangeIntersectionSmall_H
#define RangeIntersectionSmall_H

using namespace std;

class RangeIntersectionSmall {



    private:



        BInteger counter;
        BSet<BInteger > set;

    public:

        RangeIntersectionSmall() {
            counter = (BInteger(0));
            set = (BSet<BInteger>::range((BInteger(0)),(BInteger(5))));
        }

        void simulate() {
            while((counter.less((BInteger(5000000)))).booleanValue()) {
                set = set.intersect((BSet<BInteger>::range((BInteger(0)),(BInteger(5)))));
                counter = counter.plus((BInteger(1)));
            }
        }

};
int main() {
    clock_t start,finish;
    double time;
    RangeIntersectionSmall exec;
    start = clock();
    exec.simulate();
    finish = clock();
    time = (double(finish)-double(start))/CLOCKS_PER_SEC;
    printf("%f\n", time);
    return 0;
}
#endif

