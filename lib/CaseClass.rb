class CaseClass

  @@variables = nil

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
		@@variables = attrs # Guardo los atributos en una variable de clase para poder pedirlas desde afuera
		attrs.map { |attr|
			self.send('attr_reader', attr)
			self.instance_variable_set('@' + attr.to_s, ':D')
		}
	end

	def self.get_variables
		return @@variables
	end

  def self.initialize (*attrs)
    puts 'Entro al new'
    self.send('create_inmutable_instance', self.ancestors.first, attrs)
	end

  def to_s
		@class_name = self.class.ancestors.first.to_s
		@class_name +'('+@class_name.get_variables+')'
	end

end