package com.dylanpdx.retro64.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class CastleStairsBlock extends HorizontalFacingBlock {

    private static final VoxelShape SHAPE_N = VoxelShapes.union(
        Block.createCuboidShape(0, 0, 3, 16, 4, 4),
        Block.createCuboidShape(0, 0, 15, 16, 16, 16),
        Block.createCuboidShape(0, 0, 14, 16, 15, 15),
        Block.createCuboidShape(0, 0, 12, 16, 13, 13),
        Block.createCuboidShape(0, 0, 13, 16, 14, 14),
        Block.createCuboidShape(0, 0, 11, 16, 12, 12),
        Block.createCuboidShape(0, 0, 10, 16, 11, 11),
        Block.createCuboidShape(0, 0, 8, 16, 9, 9),
        Block.createCuboidShape(0, 0, 9, 16, 10, 10),
        Block.createCuboidShape(0, 0, 7, 16, 8, 8),
        Block.createCuboidShape(0, 0, 6, 16, 7, 7),
        Block.createCuboidShape(0, 0, 5, 16, 6, 6),
        Block.createCuboidShape(0, 0, 4, 16, 5, 5),
        Block.createCuboidShape(0, 0, 2, 16, 3, 3),
        Block.createCuboidShape(0, 0, 1, 16, 2, 2),
        Block.createCuboidShape(0, 0, 0, 16, 1, 1)
    );

    private static final VoxelShape SHAPE_E = VoxelShapes.union(
        Block.createCuboidShape(12, 0, 0, 13, 4, 16),
        Block.createCuboidShape(0, 0, 0, 1, 16, 16),
        Block.createCuboidShape(1, 0, 0, 2, 15, 16),
        Block.createCuboidShape(3, 0, 0, 4, 13, 16),
        Block.createCuboidShape(2, 0, 0, 3, 14, 16),
        Block.createCuboidShape(4, 0, 0, 5, 12, 16),
        Block.createCuboidShape(5, 0, 0, 6, 11, 16),
        Block.createCuboidShape(7, 0, 0, 8, 9, 16),
        Block.createCuboidShape(6, 0, 0, 7, 10, 16),
        Block.createCuboidShape(8, 0, 0, 9, 8, 16),
        Block.createCuboidShape(9, 0, 0, 10, 7, 16),
        Block.createCuboidShape(10, 0, 0, 11, 6, 16),
        Block.createCuboidShape(11, 0, 0, 12, 5, 16),
        Block.createCuboidShape(13, 0, 0, 14, 3, 16),
        Block.createCuboidShape(14, 0, 0, 15, 2, 16),
        Block.createCuboidShape(15, 0, 0, 16, 1, 16)
    );

    private static final VoxelShape SHAPE_S = VoxelShapes.union(
        Block.createCuboidShape(0, 0, 12, 16, 4, 13),
        Block.createCuboidShape(0, 0, 0, 16, 16, 1),
        Block.createCuboidShape(0, 0, 1, 16, 15, 2),
        Block.createCuboidShape(0, 0, 3, 16, 13, 4),
        Block.createCuboidShape(0, 0, 2, 16, 14, 3),
        Block.createCuboidShape(0, 0, 4, 16, 12, 5),
        Block.createCuboidShape(0, 0, 5, 16, 11, 6),
        Block.createCuboidShape(0, 0, 7, 16, 9, 8),
        Block.createCuboidShape(0, 0, 6, 16, 10, 7),
        Block.createCuboidShape(0, 0, 8, 16, 8, 9),
        Block.createCuboidShape(0, 0, 9, 16, 7, 10),
        Block.createCuboidShape(0, 0, 10, 16, 6, 11),
        Block.createCuboidShape(0, 0, 11, 16, 5, 12),
        Block.createCuboidShape(0, 0, 13, 16, 3, 14),
        Block.createCuboidShape(0, 0, 14, 16, 2, 15),
        Block.createCuboidShape(0, 0, 15, 16, 1, 16)
    );

    private static final VoxelShape SHAPE_W = VoxelShapes.union(
        Block.createCuboidShape(3, 0, 0, 4, 4, 16),
        Block.createCuboidShape(15, 0, 0, 16, 16, 16),
        Block.createCuboidShape(14, 0, 0, 15, 15, 16),
        Block.createCuboidShape(12, 0, 0, 13, 13, 16),
        Block.createCuboidShape(13, 0, 0, 14, 14, 16),
        Block.createCuboidShape(11, 0, 0, 12, 12, 16),
        Block.createCuboidShape(10, 0, 0, 11, 11, 16),
        Block.createCuboidShape(8, 0, 0, 9, 9, 16),
        Block.createCuboidShape(9, 0, 0, 10, 10, 16),
        Block.createCuboidShape(7, 0, 0, 8, 8, 16),
        Block.createCuboidShape(6, 0, 0, 7, 7, 16),
        Block.createCuboidShape(5, 0, 0, 6, 6, 16),
        Block.createCuboidShape(4, 0, 0, 5, 5, 16),
        Block.createCuboidShape(2, 0, 0, 3, 3, 16),
        Block.createCuboidShape(1, 0, 0, 2, 2, 16),
        Block.createCuboidShape(0, 0, 0, 1, 1, 16)
    );

    public CastleStairsBlock(Settings settings) {
        super(settings);
        setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    VoxelShape getShape(BlockState state) {
        switch(state.get(FACING)) {
            case NORTH:
                return SHAPE_N;
            case SOUTH:
                return SHAPE_S;
            case EAST:
                return SHAPE_E;
            case WEST:
                return SHAPE_W;
            default:
                return SHAPE_N;
        }
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return(getShape(state));
    }    

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return (BlockState)this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
    }

}
