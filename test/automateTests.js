const { exec } = require('child_process');
const path = require('path');

const runTest = (inputFileName, outputFileName, callback) => {
    const command = `java -cp ../src Main`;
    const inputFilePath = path.join(__dirname, inputFileName);
    const outputFilePath = path.join(__dirname, outputFileName);

    const child = exec(command, (error, stdout, stderr) => {
        if (error) {
            console.error(`Error executing command: ${error.message}`);
            return;
        }
        if (stderr) {
            console.error(`Error: ${stderr}`);
            return;
        }
        console.log(stdout);
        callback();
    });

    child.stdin.write(`${inputFileName}\n`);
    child.stdin.write(`yes\n`);
    child.stdin.write(`${outputFileName}\n`);
    child.stdin.end();
};

const runAllTests = () => {
    let testNumber = 1;

    const runNextTest = () => {
        if (testNumber > 10) {
            console.log('All tests completed.');
            return;
        }

        const inputFileName = `input_${testNumber}.txt`;
        const outputFileName = `output_${testNumber}.txt`;

        console.log(`Running test with ${inputFileName}...`);
        runTest(inputFileName, outputFileName, () => {
            testNumber++;
            runNextTest();
        });
    };

    runNextTest();
};

runAllTests();