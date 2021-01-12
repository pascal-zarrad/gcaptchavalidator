#!/bin/bash

#
# The MIT License
#
# Copyright (c) 2021 Pascal Zarrad
#
# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in
# all copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
# THE SOFTWARE.
#

#==================================================================
# Script Name   : rebase_current
# Description	: Script to automatically rebase current branch
#                 with develop
# Args          : -
# Author       	: Pascal Zarrad
# Email         : P.Zarrad@outlook.de
#==================================================================

set -e

# Create stash
echo "Creating stash..."
git stash

# Checkout develop, pull latest changes, switch back, rebase
echo "Checking out develop"
git checkout develop
echo "Pull latest changes from develop..."
git pull --rebase
echo "Checking out previous branch..."
git checkout -
echo "Rebasing current branch..."
git rebase develop

# Reapply stash
echo "Popping created stash..."
git stash pop

