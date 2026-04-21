package com.numdrop.core;

import com.numdrop.model.Board;

public class ChainController {

    private final MergeEngine mergeEngine;
    private final GravityEngine gravityEngine;

    public ChainController(MergeEngine mergeEngine, GravityEngine gravityEngine) {
        this.mergeEngine = mergeEngine;
        this.gravityEngine = gravityEngine;
    }

    public int resolve(Board board, int fallingCol) {

        int comboCount = 0;
        boolean changed;

        do {
            changed = false;

            if (mergeEngine.verticalMerge(board)) {
                changed = true;
            }

            if (mergeEngine.horizontalMerge(board, fallingCol)) {
                changed = true;
            }

            if (changed) {
                comboCount++;
                gravityEngine.applyGravity(board);
            }

        } while (changed);

        return comboCount;
    }

    public int resolve(Board board) {
        return resolve(board, -1);
    }
}
