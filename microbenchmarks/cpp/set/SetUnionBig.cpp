#include <iostream>
#include <string>
#include "BUtils.cpp"
#include "BSet.cpp"
#include "BInteger.cpp"
#include "BBoolean.cpp"

#ifndef SetUnionBig_H
#define SetUnionBig_H

using namespace std;

class SetUnionBig {



    private:



        BInteger counter;
        BSet<BInteger > set;

    public:

        SetUnionBig() {
            counter = (BInteger(0));
            set = (BSet<BInteger>::range((BInteger(1)),(BInteger(25000)))).difference((BSet<BInteger >((BInteger(24999)))));
        }

        void simulate() {
            while((counter.less((BInteger(10000)))).booleanValue()) {
                set = set._union((BSet<BInteger>::range((BInteger(1)),(BInteger(3000)))));
                counter = counter.plus((BInteger(1)));
            }
        }

};
int main() {
    clock_t start,finish;
    double time;
    SetUnionBig exec;
    start = clock();
    exec.simulate();
    finish = clock();
    time = (double(finish)-double(start))/CLOCKS_PER_SEC;
    printf("%f\n", time);
    return 0;
}
#endif

