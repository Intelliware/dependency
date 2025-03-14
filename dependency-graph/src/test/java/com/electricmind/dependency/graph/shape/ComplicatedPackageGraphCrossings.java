package com.electricmind.dependency.graph.shape;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.lang.SystemUtils;

import com.electricmind.dependency.DependencyManager;
import com.electricmind.dependency.graph.Grapher;

public class ComplicatedPackageGraphCrossings {

	public static void main(String[] args) throws Exception {
		new ComplicatedPackageGraphCrossings().process();
	}

	/**
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void process() throws FileNotFoundException, IOException {
		DependencyManager<String> manager = new DependencyManager<String>();
		manager.add("ca.intelliware.ereferral.converter");
		manager.add("ca.intelliware.ereferral.util.security");
		manager.add("ca.intelliware.ereferral.util");
		manager.add("ca.intelliware.ereferral.webservice.server");
		manager.add("ca.intelliware.ereferral");
		manager.add("ca.intelliware.ereferral.hibernate", "ca.intelliware.ereferral");
		manager.add("ca.intelliware.ereferral.model", "ca.intelliware.ereferral");
		manager.add("ca.intelliware.ereferral.model", "ca.intelliware.ereferral.util");
		manager.add("ca.intelliware.ereferral.config", "ca.intelliware.ereferral.model");
		manager.add("ca.intelliware.ereferral.db", "ca.intelliware.ereferral.config");
		manager.add("ca.intelliware.ereferral.util.referral",
				"ca.intelliware.ereferral.config");
		manager.add("ca.intelliware.ereferral.util.referral",
				"ca.intelliware.ereferral.model");
		manager.add("ca.intelliware.ereferral.util.referral",
				"ca.intelliware.ereferral.util");
		manager.add("ca.intelliware.ereferral.model.jasper",
				"ca.intelliware.ereferral.model");
		manager.add("ca.intelliware.ereferral.model.jasper",
				"ca.intelliware.ereferral.util.referral");
		manager.add("ca.intelliware.ereferral.message", "ca.intelliware.ereferral");

		manager.add("ca.intelliware.ereferral.message", "ca.intelliware.ereferral.model");

		manager.add("ca.intelliware.ereferral.message",
				"ca.intelliware.ereferral.util.referral");

		manager
				.add("ca.intelliware.ereferral.util.csv",
						"ca.intelliware.ereferral.model");

		manager.add("ca.intelliware.ereferral.util.csv",
				"ca.intelliware.ereferral.util.referral");

		manager.add("ca.intelliware.ereferral.service",
				"ca.intelliware.ereferral.message");

		manager.add("ca.intelliware.ereferral.service", "ca.intelliware.ereferral.model");

		manager.add("ca.intelliware.ereferral.service", "ca.intelliware.ereferral.util");

		manager.add("ca.intelliware.ereferral.service",
				"ca.intelliware.ereferral.util.csv");

		manager.add("ca.intelliware.ereferral.service",
				"ca.intelliware.ereferral.util.referral");

		manager.add("ca.intelliware.ereferral.message.hl7v3", "ca.intelliware.ereferral");

		manager.add("ca.intelliware.ereferral.message.hl7v3",
				"ca.intelliware.ereferral.config");

		manager.add("ca.intelliware.ereferral.message.hl7v3",
				"ca.intelliware.ereferral.message");

		manager.add("ca.intelliware.ereferral.message.hl7v3",
				"ca.intelliware.ereferral.model");

		manager.add("ca.intelliware.ereferral.message.hl7v3",
				"ca.intelliware.ereferral.service");

		manager.add("ca.intelliware.ereferral.web.graph",
				"ca.intelliware.ereferral.model");

		manager.add("ca.intelliware.ereferral.web.graph",
				"ca.intelliware.ereferral.service");

		manager.add("ca.intelliware.ereferral.servlet", "ca.intelliware.ereferral.model");

		manager.add("ca.intelliware.ereferral.servlet",
				"ca.intelliware.ereferral.service");

		manager.add("ca.intelliware.ereferral.servlet",
				"ca.intelliware.ereferral.util.csv");

		manager.add("ca.intelliware.ereferral.util.service",
				"ca.intelliware.ereferral.model");

		manager.add("ca.intelliware.ereferral.util.service",
				"ca.intelliware.ereferral.service");

		manager.add("ca.intelliware.ereferral.util.service",
				"ca.intelliware.ereferral.util.referral");

		manager.add("ca.intelliware.ereferral.task", "ca.intelliware.ereferral.service");

		manager.add("ca.intelliware.ereferral.dao", "ca.intelliware.ereferral");

		manager.add("ca.intelliware.ereferral.dao", "ca.intelliware.ereferral.config");

		manager.add("ca.intelliware.ereferral.dao", "ca.intelliware.ereferral.model");

		manager.add("ca.intelliware.ereferral.dao", "ca.intelliware.ereferral.service");

		manager.add("ca.intelliware.ereferral.dao", "ca.intelliware.ereferral.util.csv");

		manager.add("ca.intelliware.ereferral.dao",
				"ca.intelliware.ereferral.util.referral");

		manager.add("ca.intelliware.ereferral.message.security",
				"ca.intelliware.ereferral");

		manager.add("ca.intelliware.ereferral.message.security",
				"ca.intelliware.ereferral.config");

		manager.add("ca.intelliware.ereferral.message.security",
				"ca.intelliware.ereferral.message");

		manager.add("ca.intelliware.ereferral.message.security",
				"ca.intelliware.ereferral.message.hl7v3");

		manager.add("ca.intelliware.ereferral.message.security",
				"ca.intelliware.ereferral.util.security");

		manager.add("ca.intelliware.ereferral.controller", "ca.intelliware.ereferral");

		manager.add("ca.intelliware.ereferral.controller",
				"ca.intelliware.ereferral.config");

		manager
				.add("ca.intelliware.ereferral.controller",
						"ca.intelliware.ereferral.dao");

		manager.add("ca.intelliware.ereferral.controller",
				"ca.intelliware.ereferral.model");

		manager.add("ca.intelliware.ereferral.controller",
				"ca.intelliware.ereferral.service");

		manager.add("ca.intelliware.ereferral.controller",
				"ca.intelliware.ereferral.util");

		manager.add("ca.intelliware.ereferral.controller",
				"ca.intelliware.ereferral.util.csv");

		manager.add("ca.intelliware.ereferral.controller",
				"ca.intelliware.ereferral.util.referral");

		manager.add("ca.intelliware.ereferral.controller",
				"ca.intelliware.ereferral.util.service");

		manager.add("ca.intelliware.ereferral.jsf", "ca.intelliware.ereferral");

		manager.add("ca.intelliware.ereferral.jsf", "ca.intelliware.ereferral.dao");

		manager.add("ca.intelliware.ereferral.jsf", "ca.intelliware.ereferral.model");

		manager.add("ca.intelliware.ereferral.config.mnemonic",
				"ca.intelliware.ereferral.message.hl7v3");

		manager.add("ca.intelliware.ereferral.config.mnemonic",
				"ca.intelliware.ereferral.model");

		manager.add("ca.intelliware.ereferral.setup", "ca.intelliware.ereferral.config");

		manager.add("ca.intelliware.ereferral.setup", "ca.intelliware.ereferral.dao");

		manager.add("ca.intelliware.ereferral.setup",
				"ca.intelliware.ereferral.message.security");

		manager.add("ca.intelliware.ereferral.setup", "ca.intelliware.ereferral.model");

		manager.add("ca.intelliware.ereferral.setup",
				"ca.intelliware.ereferral.util.security");

		manager.add("ca.intelliware.ereferral.soap", "ca.intelliware.ereferral.config");

		manager.add("ca.intelliware.ereferral.soap",
				"ca.intelliware.ereferral.config.mnemonic");

		manager.add("ca.intelliware.ereferral.soap", "ca.intelliware.ereferral.dao");

		manager.add("ca.intelliware.ereferral.soap", "ca.intelliware.ereferral.message");

		manager.add("ca.intelliware.ereferral.soap",
				"ca.intelliware.ereferral.message.hl7v3");

		manager.add("ca.intelliware.ereferral.soap",
				"ca.intelliware.ereferral.message.security");

		manager.add("ca.intelliware.ereferral.soap", "ca.intelliware.ereferral.model");

		manager.add("ca.intelliware.ereferral.soap", "ca.intelliware.ereferral.service");

		manager.add("ca.intelliware.ereferral.soap",
				"ca.intelliware.ereferral.util.referral");

		manager.add("ca.intelliware.ereferral.soap",
				"ca.intelliware.ereferral.util.service");

		manager.add("ca.intelliware.ereferral.service.impl",
				"ca.intelliware.ereferral.config");

		manager.add("ca.intelliware.ereferral.service.impl",
				"ca.intelliware.ereferral.controller");

		manager.add("ca.intelliware.ereferral.service.impl",
				"ca.intelliware.ereferral.dao");

		manager.add("ca.intelliware.ereferral.service.impl",
				"ca.intelliware.ereferral.message");

		manager.add("ca.intelliware.ereferral.service.impl",
				"ca.intelliware.ereferral.message.hl7v3");

		manager.add("ca.intelliware.ereferral.service.impl",
				"ca.intelliware.ereferral.message.security");

		manager.add("ca.intelliware.ereferral.service.impl",
				"ca.intelliware.ereferral.model");

		manager.add("ca.intelliware.ereferral.service.impl",
				"ca.intelliware.ereferral.service");

		manager.add("ca.intelliware.ereferral.service.impl",
				"ca.intelliware.ereferral.soap");

		manager.add("ca.intelliware.ereferral.service.impl",
				"ca.intelliware.ereferral.util.csv");

		manager.add("ca.intelliware.ereferral.service.impl",
				"ca.intelliware.ereferral.util.referral");

		manager.add("ca.intelliware.ereferral.web", "ca.intelliware.ereferral.config");

		manager.add("ca.intelliware.ereferral.web", "ca.intelliware.ereferral.dao");

		manager.add("ca.intelliware.ereferral.web", "ca.intelliware.ereferral.model");

		manager.add("ca.intelliware.ereferral.web", "ca.intelliware.ereferral.service");

		manager.add("ca.intelliware.ereferral.web", "ca.intelliware.ereferral.soap");

		manager.add("ca.intelliware.ereferral.webservice.client",
				"ca.intelliware.ereferral");

		manager.add("ca.intelliware.ereferral.webservice.client",
				"ca.intelliware.ereferral.message");

		manager.add("ca.intelliware.ereferral.webservice.client",
				"ca.intelliware.ereferral.message.hl7v3");

		manager.add("ca.intelliware.ereferral.webservice.client",
				"ca.intelliware.ereferral.message.security");

		manager.add("ca.intelliware.ereferral.webservice.client",
				"ca.intelliware.ereferral.model");

		manager.add("ca.intelliware.ereferral.webservice.client",
				"ca.intelliware.ereferral.soap");

		File file = new File(SystemUtils.JAVA_IO_TMPDIR, "ComplicatedPackageGraphCrossings.png");
		System.out.println(file.getAbsolutePath());
		OutputStream output = new FileOutputStream(file);
		try {
			Grapher<String> grapher = new Grapher<String>(manager);
			grapher.setShape(new PackageShape<String>());
			grapher.createPng(output);
		} finally {
			output.close();
		}
		
		File svg = new File(SystemUtils.JAVA_IO_TMPDIR, "ComplicatedPackageGraphCrossings.svg");
		System.out.println(svg.getAbsolutePath());
		try (OutputStream outputSvg = new FileOutputStream(svg)) {
			Grapher<String> grapher = new Grapher<String>(manager);
			grapher.setShape(new PackageShape<String>());
			grapher.createSvg(outputSvg);
		}

	}
}
