# Budget Rocket

## Introduction

*Budget Rocket* is a Java desktop application which allows users to keep track of their income and expenses. It is a 
useful application for anyone who would like to manage their finances. This project is of interest to me because I would 
like to work for a financial institution in the future. Thus, this project is an opportunity for me to improve upon my 
skills in object-oriented software design through a financially related application. Additionally, this application is 
one I can see myself using on a daily basis to manage my own finances.

## User Stories

- As a user, I want to be able to add an expense/income to my budget  
- As a user, I want to be able to delete an expense/income from my budget
- As a user, I want to be able to modify an expense/income in my budget
- As a user, I want to be able to view a list of the expenses/incomes in my budget
- As a user, I want to be able to view the total dollar amount of expense/income in my budget
- As a user, I want to be able to save my budget to file
- As a user, I want to be able to load my budget from file

## Phase 4: Task 2

I chose to test and design a robust class in my model package. Entry and its subclasses, Expense and Income, are robust.
Specifically, the constructor will throw an exception if it is called with a negative value for the amount or an 
empty string for the description.