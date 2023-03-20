package com.group.libraryapp.calculator

import org.junit.jupiter.api.Assertions.*
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

fun main(){
    val calculatorTest = CalculatorTest()
    calculatorTest.addTest()
    calculatorTest.minusTest()
    calculatorTest.multiplyTest()
    calculatorTest.divideTest()
    calculatorTest.divideExceptionTest()
}

internal class CalculatorTest{

    fun addTest(){
        // given
        val calculator = Calculator(5)

        // when
        calculator.add(3)

//        // then
//        val expectedCalculator = Calculator(8)
//        // Calculator가 data class이기 때문에 equals가 overriding되어 있어 ==비교가 가능합니다.
//        if(calculator != expectedCalculator){
//            throw IllegalStateException()
//        }

        if(calculator.number != 8 ){
            throw IllegalStateException()
        }

    }
    fun minusTest(){
        // given
        val calculator = Calculator(5)
        // when
        calculator.minus(3)
        // then
        if(calculator.number != 2 ){
            throw IllegalStateException()
        }
    }
    fun multiplyTest(){
        // given
        val calculator = Calculator(5)
        // when
        calculator.multiply(3)
        // then
        if(calculator.number != 15 ){
            throw IllegalStateException()
        }
    }
    fun divideTest(){
        // given
        val calculator = Calculator(5)
        // when
        calculator.divide(2)
        // then
        if(calculator.number != 2 ){
            throw IllegalStateException()
        }
    }
    fun divideExceptionTest(){
        // given
        val calculator = Calculator(5)
        // when
        try{
            calculator.divide(0)
        } catch(e: IllegalArgumentException){
            if(e.message != "0으로 나눌 수 없습니다."){
                throw IllegalStateException("메시지가 다릅니다.")
            }
            // 테스트 성공
            return
        } catch(e: Exception){
            // 테스트 실패
            throw IllegalArgumentException("예상과는 다른 예외가 발생했습니다.")
        }
        // 테스트 실패
        throw IllegalArgumentException("예외가 발생하지 않았습니다")
    }



}