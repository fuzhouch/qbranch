// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.exceptions

class UnsupportedBondTypeException(cls : Class<*>, id : Int) : Exception("type=${cls.name},id=$id")