package com.aveworks.eagle.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class HomeViewModelUnitTests {

    private lateinit var viewModel : HomeViewModel

    @Mock
    private lateinit var validationObserver : Observer<Boolean>

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup(){
        viewModel = HomeViewModel()
        viewModel.getXPubValidation().observeForever(validationObserver)
    }

    @Test
    fun init_shouldBeInvalid(){
        verify(validationObserver).onChanged(eq(false))
    }

    @Test
    fun changingBetween_invalidValidValues_emitOnlyOneEvent(){
        viewModel.setXPub("1")
        viewModel.setXPub("2")
        viewModel.setXPub("3")
        verify(validationObserver).onChanged(eq(false))
    }

    @Test
    fun changingBetweenValues_emitTwoEvents(){
        viewModel.setXPub("1")
        viewModel.setXPub("valid")
        viewModel.setXPub("3")
        verify(validationObserver, times(1)).onChanged(eq(true))
        verify(validationObserver, times(2)).onChanged(eq(false))
    }

    @Test
    fun validInput_shouldBeValid(){
        viewModel.setXPub("1")
        viewModel.setXPub("2")
        viewModel.setXPub("valid")
        verify(validationObserver).onChanged(eq(true))
    }
}