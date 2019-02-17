package ejektaflex.bountiful.gui

import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.resources.I18n
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.inventory.Container
import net.minecraft.util.ResourceLocation

class GuiBoard(container: Container, private val playerInv: InventoryPlayer) : GuiContainer(container) {

    override fun drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int) {
        GlStateManager.color4f(1f, 1f, 1f, 1f)
        mc.textureManager.bindTexture(BACKGROUND)
        val x = (width - xSize) / 2
        val y = (height - ySize) / 2
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize)
    }

    override fun drawGuiContainerForegroundLayer(mouseX: Int, mouseY: Int) {
        val name = I18n.format("bountiful.bounty.board")
        fontRenderer.drawString(name, (xSize / 2 - fontRenderer.getStringWidth(name) / 2).toFloat(), 6f, 0x505050)
        fontRenderer.drawString(playerInv.displayName.unformattedComponentText, 8f, (ySize - 95).toFloat(), 0x505050)
        super.drawGuiContainerForegroundLayer(mouseX, mouseY)
    }

    // TODO Implement board draw functions
    /*
    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        this.drawDefaultBackground()
        super.drawScreen(mouseX, mouseY, partialTicks)
        renderHoveredToolTip(mouseX, mouseY)
    }
    */

    companion object {
        private val BACKGROUND = ResourceLocation("minecraft", "textures/gui/container/shulker_box.png")
    }

}
