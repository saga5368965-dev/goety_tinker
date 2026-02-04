package saga.goety_tinker.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.model.data.ModelData;
import saga.goety_tinker.Goety_tinker;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.List;

public class SpecialStaffRenderer extends BlockEntityWithoutLevelRenderer {

    private static final ModelResourceLocation HANDLE_MODEL = new ModelResourceLocation(new ResourceLocation(Goety_tinker.MODID, "staff_handle"), "inventory");
    private static final ModelResourceLocation HEAD_MODEL = new ModelResourceLocation(new ResourceLocation(Goety_tinker.MODID, "staff_head"), "inventory");
    private static final ModelResourceLocation CORE_MODEL = new ModelResourceLocation(new ResourceLocation(Goety_tinker.MODID, "staff_core"), "inventory");

    public SpecialStaffRenderer(BlockEntityRenderDispatcher dispatcher, EntityModelSet modelSet) {
        super(dispatcher, modelSet);
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        ToolStack tool = ToolStack.from(stack);
        Minecraft mc = Minecraft.getInstance();

        BakedModel handleModel = mc.getModelManager().getModel(HANDLE_MODEL);
        BakedModel headModel = mc.getModelManager().getModel(HEAD_MODEL);
        BakedModel coreModel = mc.getModelManager().getModel(CORE_MODEL);

        poseStack.pushPose();
        poseStack.translate(0.5f, 0.5f, 0.5f);
        renderPartWithTicColor(poseStack, buffer, handleModel, stack, combinedLight, combinedOverlay);
        renderPartWithTicColor(poseStack, buffer, headModel, stack, combinedLight, combinedOverlay);
        poseStack.pushPose();
        float rotation = (System.currentTimeMillis() % 4000) / 4000f * 360f;
        poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
        renderPartWithTicColor(poseStack, buffer, coreModel, stack, combinedLight, combinedOverlay);
        poseStack.popPose();

        poseStack.popPose();
    }
    private void renderPartWithTicColor(PoseStack poseStack, MultiBufferSource buffer, BakedModel model, ItemStack stack, int light, int overlay) {
        RandomSource random = RandomSource.create();
        random.setSeed(42L);
        List<BakedQuad> quads = model.getQuads(null, null, random, ModelData.EMPTY, null);

        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.cutout());

        for (BakedQuad quad : quads) {
            float r = 1.0f, g = 1.0f, b = 1.0f;

            if (quad.isTinted() && !stack.isEmpty()) {
                int color = Minecraft.getInstance().getItemColors().getColor(stack, quad.getTintIndex());
                if (color != -1) {
                    r = ((color >> 16) & 0xFF) / 255.0f;
                    g = ((color >> 8) & 0xFF) / 255.0f;
                    b = (color & 0xFF) / 255.0f;
                }
            }

            vertexConsumer.putBulkData(
                    poseStack.last(),
                    quad,
                    r, g, b, 1.0f,
                    light,
                    overlay,
                    true
            );
        }
    }
}