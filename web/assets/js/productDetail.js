function updateVariantInfo(selectEl) {
    const selectedOption = selectEl.options[selectEl.selectedIndex];
    const form = selectEl.closest('form');
    const variantID = selectedOption.getAttribute('data-variant-id');
    const stock = selectedOption.getAttribute('data-stock');

    form.querySelector('input[name="variantID"]').value = variantID;
    form.querySelector('input[name="maxStock"]').value = stock;

    const qtyInput = form.querySelector('input[name="quantity"]');
    qtyInput.max = stock;
    if (parseInt(qtyInput.value) > parseInt(stock)) qtyInput.value = stock;
}
