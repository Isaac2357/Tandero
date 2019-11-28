package com.iteso.tanderomobile.fragments.admin;

/**Interface that edits and deletes tandas. */
public interface OnTandaClickListener {
    /**Edit Tanda method.
     * @param tanda  tanda name.*/
    void onEditButtonClick(String tanda);
    /**Delete Tanda method.
     * @param tanda tanda name.*/
    void onDeleteButtonClick(String tanda);
}
