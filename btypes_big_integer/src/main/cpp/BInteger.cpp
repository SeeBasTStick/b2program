#include <iostream>
#include <string>
#include "BBoolean.cpp"
#include <gmpxx.h>

#ifndef BINTEGER_H
#define BINTEGER_H

using namespace std;

class BInteger : public BObject {

    /*
	@Override
	public boolean equals(Object obj) {
		if (== obj)
			return true;
		if (obj == null)
			return false;
		if (obj instanceof java.lang.Number) {
			return compareTo((java.lang.Number) obj) == 0;
		}
		// assert getClass() != obj.getClass()
		return false;
	}*/

	private:
	    mpz_class value;

	/*@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}*/

    public:

        typedef void value_type;
        typedef void left_type;
        typedef void right_type;

        BInteger(const char* val) {
            value = val;
        }

        BInteger(const int& val) {
            value = val;
        }

        BInteger(const mpz_class& val) {
            value = val;
        }

        int intValue() const {
            return value.get_si();
        }

        BInteger(){};

        BInteger(const BInteger& val) {
            value = val.value;
        }


        BBoolean lessEqual(const BInteger& o) const {
            return value <= o.value;
        }

        BBoolean greaterEqual(const BInteger& o) const {
            return value >= o.value;
        }

        BBoolean less(const BInteger& o) const {
            return value < o.value;
        }

        BBoolean greater(const BInteger& o) const {
            return value > o.value;
        }

        BBoolean equal(const BInteger& o) const {
            return value == o.value;
        }

        BBoolean unequal(const BInteger& o) const {
            return value != o.value;
        }

        /*int compareTo(java.lang.Number o) {
            java.math.BigInteger oi;
            if (o == null) {
                throw new NullPointerException();
            }
            if (getClass() != o.getClass()) {
                oi = new java.math.BigInteger(java.lang.String.valueOf(o.longValue()));
            } else {
                BInteger oo = (BInteger) o;
                oi = oo.value;
            }
            return value.compareTo(oi);
        }*/

        /*long longValue() {
            return value.longValue();
        }

        float floatValue() {
            return value.floatValue();
        }

        double doubleValue() {
            return value.doubleValue();
        }*/

        BInteger plus(const BInteger& o) const {
            return BInteger(value + o.value);
        }

        /*java.math.BigInteger asBigInteger() {
            return value;
        }

        java.lang.String toString() {
            return value.toString();
        }*/

        BInteger minus(const BInteger& o) const {
            return BInteger(value - o.value);
        }

        BInteger multiply(const BInteger& o) const {
            return BInteger(value * o.value);
        }

        BInteger divide(const BInteger& o) const {
            return BInteger(value / o.value);
        }

        BInteger modulo(const BInteger& o) const {
            return BInteger(value % o.value);
        }

        /*BInteger or(BInteger o) {
            return new BInteger(value.or(o.value));
        }

        BInteger and(BInteger o) {
            return new BInteger(value.and(o.value));
        }

        BInteger xor(BInteger o) {
            return new BInteger(value.xor(o.value));
        }*/

        BInteger succ() const {
            return BInteger(value + 1);
        }

        BInteger pred() const {
            return BInteger(value - 1);
        }

        /*BInteger leftShift(BInteger o) {
            return new BInteger(value.shiftLeft(o.intValue()));
        }

        BInteger rightShift(BInteger o) {
            return new BInteger(value.shiftRight(o.intValue()));
        }

        boolean isCase(BInteger o) {
            return equals(o);
        }*/

        BInteger negative() const {
            return BInteger(-value);
        }

        BInteger positive() const {
            return *this;
        }

        /*BInteger operator=(BInteger other) {
            this->value = other.value;
            return *this;
        }*/

        friend bool operator !=(const BInteger& o1, const BInteger& o2) {
            return o1.value != o2.value;
        }

        friend bool operator ==(const BInteger& o1, const BInteger& o2) {
            return o1.value == o2.value;
        }

        void operator =(const BInteger& other) {
            value = other.value;
        }

        int hashCode() const {
            int prime = 31;
            int result = 1;
            result = prime * result + value.get_si();
            return result;
        }

        friend std::ostream& operator<<(std::ostream &strm, const BInteger &b) {
          return strm << b.value;
        }

};
#endif