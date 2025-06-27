package net.aros.playerplushies.block;

import net.aros.playerplushies.init.AppSounds;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class PlushieBlock extends Block {
    public static final VoxelShape SHAPE_BASE = createCuboidShape(4, 0, 4, 12, 14.5, 12);
    public static final VoxelShape SHAPE_SOUTH = SHAPE_BASE.offset(0, 0, -0.05);
    public static final VoxelShape SHAPE_NORTH = SHAPE_BASE.offset(0, 0, 0.05);
    public static final VoxelShape SHAPE_EAST = SHAPE_BASE.offset(-0.05, 0, 0);
    public static final VoxelShape SHAPE_WEST = SHAPE_BASE.offset(0.05, 0, 0);

    public PlushieBlock(Settings settings) {
        super(settings.blockVision((a, b, c) -> false).nonOpaque());
        setDefaultState(getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(Properties.HORIZONTAL_FACING)) {
            case SOUTH -> SHAPE_SOUTH;
            case EAST -> SHAPE_EAST;
            case WEST -> SHAPE_WEST;
            case null, default -> SHAPE_NORTH;
        };
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(Properties.HORIZONTAL_FACING)) {
            case SOUTH -> SHAPE_SOUTH;
            case EAST -> SHAPE_EAST;
            case WEST -> SHAPE_WEST;
            case null, default -> SHAPE_NORTH;
        };
    }

    @Override
    protected VoxelShape getCullingShape(BlockState state, BlockView world, BlockPos pos) {
        return switch (state.get(Properties.HORIZONTAL_FACING)) {
            case SOUTH -> SHAPE_SOUTH;
            case EAST -> SHAPE_EAST;
            case WEST -> SHAPE_WEST;
            case null, default -> SHAPE_NORTH;
        };
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(Properties.HORIZONTAL_FACING);
    }

    @Override
    protected ActionResult onUse(BlockState state, @NotNull World world, BlockPos pos, PlayerEntity user, BlockHitResult hit) {
        if (!world.isClient) {
            world.playSound(null, user.getBlockPos(), AppSounds.PLUSHIE_USE.get(), SoundCategory.PLAYERS, 1.0f, 0.5f + world.random.nextFloat() * 1.2f);
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public BlockState getPlacementState(@NotNull ItemPlacementContext ctx) {
        return this.getDefaultState().with(Properties.HORIZONTAL_FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    public BlockState rotate(@NotNull BlockState state, @NotNull BlockRotation rotation) {
        return state.with(Properties.HORIZONTAL_FACING, rotation.rotate(state.get(Properties.HORIZONTAL_FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, @NotNull BlockMirror mirror) {
        return rotate(state, mirror.getRotation(state.get(Properties.HORIZONTAL_FACING)));
    }
}
