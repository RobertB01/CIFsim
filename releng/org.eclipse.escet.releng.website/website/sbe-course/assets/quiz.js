/******************************************************************************
 * Copyright (c) 2024 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the terms
 * of the MIT License which is available at https://opensource.org/licenses/MIT
 *
 * SPDX-License-Identifier: MIT
 *****************************************************************************/

function buildQuizzes() {
    // Replace contents of HTML elements using the 'quiz' class with a dynamically generated quiz.
    const quizzes = document.getElementsByClassName('quiz');
    for (let i = 0; i < quizzes.length; i++) {
        buildQuiz(quizzes[i], 'quiz' + i);
    }
}

function buildQuiz(quizElement, quizId) {
    // Get quiz questions and answers from the JSON data in the HTML element's body.
    let questions = eval(quizElement.innerHTML);

    // Clear the HTML element's body, such that we can add new HTML elements for the quiz questions and answers.
    quizElement.innerHTML = '';

    // Generate HTML elements for questions and answers.
    const output = [];
    questions.forEach((currentQuestion, questionNumber) => {
        const answers = [];
        for (let i = 0; i < currentQuestion.answers.length; i++) {
            let answer = currentQuestion.answers[i];
            if (currentQuestion.type === 'single-choice') {
                // Create radio inputs for single-choice question.
                answers.push(
                    `<div class="quiz-answer">
                        <div><input type='radio' name='${quizId}-question${questionNumber}' id='${quizId}-question${questionNumber}-${i + 1}' value='${i + 1}'></div>
                        <label for='${quizId}-question${questionNumber}-${i + 1}'>${answer}</label>
                    </div>`
                );
            } else if (currentQuestion.type === 'multiple-choice') {
                // Create checkboxes for multiple-choice question.
                answers.push(
                    `<div class="quiz-answer">
                        <div><input type='checkbox' name='${quizId}-question${questionNumber}' id='${quizId}-question${questionNumber}-${i + 1}' value='${i + 1}'></div>
                        <label for='${quizId}-question${questionNumber}-${i + 1}'>${answer}</label>
                    </div>`
                );
            } else {
                console.error('Unknown question type: ' + currentQuestion.type);
            }
        }

        // Add this question and its answers to the output.
        output.push(
            `<div id='${quizId}-question${questionNumber}' class='quiz-question-wrap'>
                 <div class='quiz-question'>${questionNumber + 1}. ${currentQuestion.question}</div>
                 <div class='quiz-answers'>${answers.join('')}</div>
                 <div class='quiz-question-feedback'></div>
             </div>`
        );
    });

    // Fill the HTML placeholder element with our generated HTML, and a submit button, and a placeholder for the quiz
    // feedback.
    const quizDiv = document.createElement('div');
    quizElement.appendChild(quizDiv);

    const submitButton = document.createElement('button');
    submitButton.textContent = 'Check answers';
    submitButton.classList.add('submit');
    submitButton.classList.add('btn');
    submitButton.classList.add('escet-btn');
    quizElement.appendChild(submitButton);

    const quizFeedbackDiv = document.createElement('div');
    quizFeedbackDiv.classList.add('quiz-feedback');
    quizElement.appendChild(quizFeedbackDiv);

    quizDiv.innerHTML = output.join('');

    // Mark the quiz is having been built.
    quizElement.classList.add('quiz-built');

    // Add click event for the submit button, to check answers and provide feedback.
    submitButton.addEventListener('click', () => {
        // Get the answers provided by the user.
        const answerContainers = quizDiv.querySelectorAll('.quiz-answers');
        const questionFeedbackDivs = quizDiv.querySelectorAll('.quiz-question-feedback');

        // Track correct answers.
        let numCorrect = 0;

        // Find the provided answer(s) for each question and check it/them against the correct answer(s).
        questions.forEach((currentQuestion, questionNumber) => {
            // Find the selected answer(s).
            const answerContainer = answerContainers[questionNumber];
            const questionFeedbackDiv = questionFeedbackDivs[questionNumber];
            const selector = `input[name=${quizId}-question${questionNumber}]:checked`;
            let userAnswer;
            if (currentQuestion.type === 'single-choice') {
                // Single-choice question.
                userAnswer = (answerContainer.querySelector(selector) || {}).value;
            } else if (currentQuestion.type === 'multiple-choice') {
                // Multiple-choice question.
                userAnswer = Array.from(answerContainer.querySelectorAll(selector)).map(x => x.value).join(', ');
            } else {
                console.error('Unknown question type: ' + currentQuestion.type);
            }

            if (userAnswer === currentQuestion.correctAnswer) {
                // The answer(s) is/are correct, so count it as a correctly answered question.
                numCorrect++;

                // Provide feedback on the correctly answered question.
                answerContainer.classList.remove('quiz-incorrect-answer');
                answerContainer.classList.add('quiz-correct-answer');
                questionFeedbackDiv.classList.remove('quiz-incorrect-answer');
                questionFeedbackDiv.classList.add('quiz-correct-answer');
                questionFeedbackDiv.innerHTML = 'The answer is correct.';
            } else {
                // The answer(s) is/are incorrect, or was not filled in.
                // Provide feedback on the incorrectly answered question.
                answerContainer.classList.remove('quiz-correct-answer');
                answerContainer.classList.add('quiz-incorrect-answer');
                questionFeedbackDiv.classList.remove('quiz-correct-answer');
                questionFeedbackDiv.classList.add('quiz-incorrect-answer');
                questionFeedbackDiv.innerHTML = 'The answer is incorrect.';
            }
        });

        // Provide feedback on the entire quiz.
        feedback = 'You answered ' + numCorrect + ' out of ' + questions.length + ' question' + (questions.length > 1 ? 's' : '') + ' correctly.';
        if (numCorrect === questions.length) {
            quizFeedbackDiv.classList.remove('quiz-incorrect-answer');
            quizFeedbackDiv.classList.add('quiz-correct-answer');
            feedback += ' Please proceed!';
        } else {
            quizFeedbackDiv.classList.remove('quiz-correct-answer');
            quizFeedbackDiv.classList.add('quiz-incorrect-answer');
            feedback += ' Please study the previous section again and then try the quiz once more.';
        }
        quizFeedbackDiv.innerHTML = `${feedback}`;
        resetAnimation(quizFeedbackDiv);
    });
}

function resetAnimation(element) {
    element.style.animation = 'none'; // Disable animation.
    element.offsetHeight; // Trigger reflow.
    element.style.animation = null; // Restore animation.
}
