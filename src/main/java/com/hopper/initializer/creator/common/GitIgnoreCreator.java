/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.hopper.initializer.creator.common;

import com.hopper.initializer.FileProcessor;
import com.hopper.initializer.creator.FileCreator;
import com.hopper.initializer.creator.annotation.JavaLibrary;
import com.hopper.initializer.creator.annotation.Node;
import com.hopper.initializer.creator.annotation.React;
import com.hopper.initializer.creator.annotation.SpringBoot;
import com.hopper.initializer.model.ProjectCreation;
import com.hopper.model.ApplicationType;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Paths;

import static com.google.common.base.Preconditions.checkNotNull;

@Component
@Slf4j
@SpringBoot
@React
@Node
@JavaLibrary
public class GitIgnoreCreator implements FileCreator<ProjectCreation> {
    static final String GIT_IGNORE_TEMPLATE_PATH = "projectCreation/gitignore.tpl";
    private final FileProcessor fileProcessor;

    public GitIgnoreCreator(FileProcessor fileProcessor) {
        this.fileProcessor = checkNotNull(fileProcessor);
    }

    @Override
    public void create(ProjectCreation request) {
        log.info("Creating .gitignore file");
        File file = this.fileProcessor.touch(Paths.get(request.getRootDir(), ".gitignore"));
        writeContentTo(file, request);
    }
    private void writeContentTo(File file, ProjectCreation request) {
        String content = fileProcessor.processTemplate(GIT_IGNORE_TEMPLATE_PATH, ImmutableMap.of("IS_REACT", request.getType() == ApplicationType.REACT, "IS_NODE", request.getType() == ApplicationType.NODE, "IS_JAVA_BASED", isJavaBasedProject(request)));
        fileProcessor.writeContentTo(file, content);
    }

    private boolean isJavaBasedProject(ProjectCreation request) {
        return request.isJavaBasedProject();
    }
}