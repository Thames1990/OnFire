package xyz.thomasmohr.onfire.data

import android.support.v7.util.DiffUtil

data class CountersWithDiff(val counters: List<Counter>, val diff: DiffUtil.DiffResult?)