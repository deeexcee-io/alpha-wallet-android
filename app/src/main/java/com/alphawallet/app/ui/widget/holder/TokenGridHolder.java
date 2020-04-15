package com.alphawallet.app.ui.widget.holder;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alphawallet.app.R;
import com.alphawallet.app.entity.opensea.Asset;
import com.alphawallet.app.entity.tokens.ERC721Ticket;
import com.alphawallet.app.entity.tokens.ERC721Token;
import com.alphawallet.app.entity.tokens.Token;
import com.alphawallet.app.service.AssetDefinitionService;
import com.alphawallet.app.ui.widget.OnTokenClickListener;
import com.alphawallet.app.util.Utils;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TokenGridHolder extends BinderViewHolder<Token> {

    public static final int VIEW_TYPE = 2005;

    private final LinearLayout layout;
    private final TextView name;
    private final ImageView imageIcon;
    private final TextView textIcon;
    private final AssetDefinitionService assetDefinition;

    private OnTokenClickListener onTokenClickListener;

    public TokenGridHolder(int resId, ViewGroup parent, AssetDefinitionService assetService) {
        super(resId, parent);

        layout = findViewById(R.id.token_layout);
        imageIcon = findViewById(R.id.token_icon);
        name = findViewById(R.id.token_name);
        textIcon = findViewById(R.id.text_icon);

        assetDefinition = assetService;
    }

    @Override
    public void bind(@Nullable Token token, @NonNull Bundle addition) {
        if (token != null) {
            if (token.isERC721()) {
                ERC721Token tkn = (ERC721Token) token;
                List<Asset> assets = tkn.getTokenAssets();
                if (assets != null) {
                    Asset firstAsset = assets.get(0);
                    if (firstAsset != null) {
                        Glide.with(getContext())
                                .load(firstAsset.getImagePreviewUrl())
                                .into(imageIcon);
                        name.setText(token.tokenInfo.name);
                        textIcon.setVisibility(View.GONE);
                        imageIcon.setVisibility(View.VISIBLE);
                    } else {
                        setupIcon(token);
                    }
                }
            } else if (token.isERC721Ticket()) {
                ERC721Ticket tkn = (ERC721Ticket) token;
                String tokenName = tkn.getTokenName(assetDefinition, 0);
                name.setText(tokenName);
                setupIcon(token);
            } else {
                name.setText(token.tokenInfo.name);
                setupIcon(token);
            }

            layout.setOnClickListener(v -> {
                if (onTokenClickListener != null) {
                    onTokenClickListener.onTokenClick(v, token, null, true);
                }
            });
        }
    }

    private void setupIcon(@NotNull Token token) {
        imageIcon.setVisibility(View.GONE);
        textIcon.setVisibility(View.VISIBLE);
        textIcon.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), Utils.getChainColour(token.tokenInfo.chainId)));
        textIcon.setText(Utils.getIconisedText(token.tokenInfo.name));
    }

    public void setOnTokenClickListener(OnTokenClickListener onTokenClickListener) {
        this.onTokenClickListener = onTokenClickListener;
    }
}