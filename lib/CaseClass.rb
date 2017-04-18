class CaseClass

  @@variables = 'Todavia no le puso nada'

	def metodo_de_la_caseclass
		'hola'
	end

  def self.< (sym)
		return (self.ancestors.first.to_s + '~' + sym.to_s).to_s
	end

  def self.is_case_class?
		true
	end

  def self.attr_accessor (*attrs)
		@@variables = attrs
		attrs.map { |attr|
			self.send('attr_reader', attr)
			self.instance_variable_set('@' + attr.to_s, ':D')
		}
	end

	def self.get_variables
		return @@variables
	end

end