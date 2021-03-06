/*
 * This file is part of UltimateCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) Bammerbom
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package bammerbom.ultimatecore.sponge.modules.blockprotection.api.locktype;

import org.spongepowered.api.CatalogKey;
import org.spongepowered.api.registry.CatalogRegistryModule;

import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

public class LockTypeRegistry implements CatalogRegistryModule<LockType> {

    Map<String, LockType> mapping = new HashMap<>();

    @Override
    public void registerDefaults() {
        mapping.put("private", LockTypes.PRIVATE);
        mapping.put("password", LockTypes.PASSWORD);
        mapping.put("public", LockTypes.PUBLIC);
    }

    @Override
    public Optional<LockType> get(CatalogKey key) {
        return Optional.ofNullable(this.mapping.get(checkNotNull(key).getValue().toLowerCase(Locale.ENGLISH)));
    }

    @Override
    public Collection<LockType> getAll() {
        return mapping.values();
    }
}
