#!/bin/bash

# The MIT License
#
# Copyright (c) 2020 Pascal Zarrad
#
# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
#  furnished to do so, subject to the following conditions:
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

#==================================================================
# Script Name   : rebase_develop
# Description	: Script to automatically rebase develop branch
#                 after a release has happened.
# Args          : -
# Author       	: Pascal Zarrad
# Email         : P.Zarrad@outlook.de
#==================================================================

# ATTENTION: This script will reset master and develop to the current remote state.
#            Local changes will be lost!

set -e

# Simple yes/no prompt
confirm() {
    read -r -p "${1:-Are you sure? [y/N]} " response
    case "$response" in
        [yY][eE][sS]|[yY])
            true
            ;;
        *)
            false
            ;;
    esac
}

# Ensure that the user really wants to continue
if ! confirm "This script will reset your local master and develop branches, do you really want to run it? [y/N]"
    then
        exit
fi

# Create stash
echo "Creating stash..."
git stash

# Fetch remote
echo "Fetching remote changes..."
git fetch origin

# Checkout master, update local branch and switch back
echo "Checking out master..."
git checkout master
echo "Resetting local master to remote master..."
git reset --hard origin/master
echo "Checking out previous branch..."
git checkout -
echo ""

# Checkout develop, pull latest changes, rebase, push updated branch, switch back
echo "Checking out develop"
git checkout develop
echo "Resetting local develop to remote develop..."
git reset --hard origin/develop
echo "Rebasing develop..."
git rebase master
echo "Pushing changes to develop..."
git push --force-with-lease
echo "Checking out previous branch..."
git checkout -

# Reapply stash
echo "Popping created stash..."
git stash pop

