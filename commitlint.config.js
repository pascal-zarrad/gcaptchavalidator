/**
 * The MIT License
 *
 * Copyright (c) 2020 Pascal Zarrad
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

// Configuration of commitlint to check commit message guidelines
module.exports = {
	parserPreset: 'conventional-changelog-conventionalcommits',
	rules: {
		'subject-max-length': [2, 'always', 50],
		'subject-case': [
			2,
			'never',
			['sentence-case', 'start-case'],
		],
		'subject-empty': [2, 'never'],
		'subject-full-stop': [2, 'never', '.'],
		'type-case': [2, 'always', 'lower-case'],
		'type-empty': [2, 'never'],
		'type-enum': [
			2,
			'always',
			[
                'feat',
                'fix',
                'perf',
                'refactor',
                'cs',
                'test',
                'build',
                'ci',
                'docs',
                'changelog'
			],
		],
		'scope-empty': [2, 'always'],
		'header-max-length': [2, 'always', 75],
		'body-leading-blank': [1, 'always'],
		'body-max-line-length': [2, 'always', 75],
		'footer-leading-blank': [1, 'always'],
		'footer-max-line-length': [2, 'always', 75]
	},
};
