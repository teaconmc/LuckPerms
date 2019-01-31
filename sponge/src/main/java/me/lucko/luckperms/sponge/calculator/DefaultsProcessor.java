/*
 * This file is part of LuckPerms, licensed under the MIT License.
 *
 *  Copyright (c) lucko (Luck) <luck@lucko.me>
 *  Copyright (c) contributors
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package me.lucko.luckperms.sponge.calculator;

import me.lucko.luckperms.api.Tristate;
import me.lucko.luckperms.api.context.ImmutableContextSet;
import me.lucko.luckperms.common.calculator.processor.PermissionProcessor;
import me.lucko.luckperms.common.calculator.result.TristateResult;
import me.lucko.luckperms.sponge.service.model.LPPermissionService;
import me.lucko.luckperms.sponge.service.model.LPSubject;

public abstract class DefaultsProcessor implements PermissionProcessor {
    private static final TristateResult.Factory RESULT_FACTORY = new TristateResult.Factory(DefaultsProcessor.class);

    protected final LPPermissionService service;
    private final ImmutableContextSet contexts;

    public DefaultsProcessor(LPPermissionService service, ImmutableContextSet contexts) {
        this.service = service;
        this.contexts = contexts;
    }

    protected abstract LPSubject getTypeDefaults();

    @Override
    public TristateResult hasPermission(String permission) {
        Tristate t = getTypeDefaults().getPermissionValue(this.contexts, permission);
        if (t != Tristate.UNDEFINED) {
            return RESULT_FACTORY.result(t, "type defaults");
        }

        t = this.service.getRootDefaults().getPermissionValue(this.contexts, permission);
        if (t != Tristate.UNDEFINED) {
            return RESULT_FACTORY.result(t, "root defaults");
        }

        return TristateResult.UNDEFINED;
    }
}
